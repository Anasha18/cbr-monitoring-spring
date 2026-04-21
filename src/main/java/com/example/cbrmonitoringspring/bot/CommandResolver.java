package com.example.cbrmonitoringspring.bot;


import com.example.cbrmonitoringspring.bot.command.*;
import com.example.cbrmonitoringspring.bot.exception.CommandNotFoundException;
import com.example.cbrmonitoringspring.bot.statemachine.BotStateMachine;
import com.example.cbrmonitoringspring.service.CurrencyService;
import com.example.cbrmonitoringspring.service.SubscriptionService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommandResolver {
    private final Map<String, Command> commands;

    public CommandResolver(
            CurrencyService currencyService,
            SubscriptionService subscriptionService,
            BotStateMachine botStateMachine
    ) {
        this.commands = Map.of(
                "/start", new StarCommand(),
                "/currency", new CurrencyCommand(currencyService),
                "/subscribe", new SubscribeCommand(subscriptionService),
                "/unsubscribe", new UnsubscribeCommand(subscriptionService),
                "/conversion", new ConversionCommand(botStateMachine),
                "/cancel", new StarCommand()
        );
    }

    public CommandResult resolve(String message) {
        String parsedMessage = message.trim();
        if (parsedMessage.isEmpty()) {
            throw new CommandNotFoundException();
        }

        String[] parts = parsedMessage.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

        Command command = commands.get(commandName);
        if (command == null) {
            throw new CommandNotFoundException();
        }

        return new CommandResult(command, args);
    }

    public record CommandResult(Command command, String[] args) {
    }
}
