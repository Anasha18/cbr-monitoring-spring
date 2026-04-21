package com.example.cbrmonitoringspring.bot.command;

public interface Command {
    String execute(Long telegramId, String... args);
}
