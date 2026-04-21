package com.example.cbrmonitoringspring.bot.runner;

import com.example.cbrmonitoringspring.bot.CbrMonitoringBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
@RequiredArgsConstructor
public class BotRunner implements SmartLifecycle {
    private final CbrMonitoringBot bot;
    private volatile BotSession botSession;
    private volatile boolean running;

    @Override
    public synchronized void start() {
        if (running) {
            return;
        }
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botSession = botsApi.registerBot(bot);
            running = true;
            log.info("Telegram bot started: {}", bot.getBotUsername());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start telegram bot", e);
        }
    }

    @Override
    public synchronized void stop() {
        if (botSession != null) {
            botSession.stop();
            botSession = null;
        }
        running = false;
        log.info("Telegram bot stopped");
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

}
