package com.example.cbrmonitoringspring.bot.command;

import com.example.cbrmonitoringspring.bot.statemachine.BotStateMachine;
import com.example.cbrmonitoringspring.bot.statemachine.StateMachineResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ConversionCommand implements Command {
    public static final String CONVERSION_FLOW_KEY = "conversion-flow";
    private final BotStateMachine botStateMachine;

    @Override
    public String execute(Long telegramId, String... args) {
        if (args.length == 0 || !"старт".equalsIgnoreCase(args[0])) {
            return "Формат: /conversion старт";
        }

        StateMachineResult stateMachineResult = botStateMachine.startFlow(telegramId, CONVERSION_FLOW_KEY);

        return switch (stateMachineResult) {
            case STARTED -> """
                    Conversion запущен.
                    Напиши код валюты откуда переводить
                    """.trim();
            case ALREADY_RUNNING -> """
                    Продолжи шаг.
                    """;
            case NOT_IMPLEMENTED -> """
                    У разработчика  бота пока лапки и он не дошел до реализации, пупу.
                    """;
        };
    }
}
