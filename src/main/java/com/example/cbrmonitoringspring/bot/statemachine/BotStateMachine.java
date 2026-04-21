package com.example.cbrmonitoringspring.bot.statemachine;

import java.util.Optional;

public interface BotStateMachine {

    StateMachineResult startFlow(Long chatId, String text);

    boolean hasActiveFlow(Long chatId);

    Optional<String> tryHandle(Long chatId, String text);
}
