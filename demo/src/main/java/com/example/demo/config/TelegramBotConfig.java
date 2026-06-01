package com.emergency.alert.config;

import com.emergency.alert.telegram.EmergencyTelegramBot;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {

    private final EmergencyTelegramBot bot;

    @PostConstruct
    public void registerBot() throws Exception {

        TelegramBotsApi api =
                new TelegramBotsApi(
                        DefaultBotSession.class
                );

        api.registerBot(bot);
    }
}