package com.example.cbrmonitoringspring.bot;

import com.example.cbrmonitoringspring.bot.exception.CommandNotFoundException;
import com.example.cbrmonitoringspring.bot.statemachine.BotStateMachine;
import com.example.cbrmonitoringspring.configuration.BusinessLogicProperties;
import com.example.cbrmonitoringspring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class CbrMonitoringBot extends TelegramLongPollingBot {
    private final BusinessLogicProperties config;
    private final UserService userService;
    private final CommandResolver commandResolver;
    private final BotStateMachine botStateMachine;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText() || update.getMessage().getFrom() == null) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        User from = update.getMessage().getFrom();

        var user = userService.getOrCreateUserByTelegramId(from.getUserName(), chatId);

        if (botStateMachine.hasActiveFlow(chatId)) {
            var flowReply = botStateMachine.tryHandle(user.getTelegramId(), text);
            if (flowReply.isPresent()) {
                sendMessage(chatId, flowReply.get());
                return;
            }
        }

        try {
            CommandResolver.CommandResult result = commandResolver.resolve(text);
            String response = result.command().execute(chatId, result.args());

            sendMessage(chatId, response);
        } catch (CommandNotFoundException e) {
            sendMessage(chatId, e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process message. chatId={}", chatId, e);
            sendMessage(chatId, "Произошла ошибка при обработке команды.");
        }
    }

    @Override
    public String getBotUsername() {
        return config.getTelegram().username();
    }

    @Override
    public String getBotToken() {
        return config.getTelegram().token();
    }

    public void sendMessage(Long chatId, String text) {
        try {
            execute(buildMessage(chatId, text));
        } catch (Exception e) {
            log.error("Failed to send message. chatId={}", chatId, e);
        }

    }

    private SendMessage buildMessage(Long chatId, String text) {
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
