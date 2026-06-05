package com.emergency.alert.config;

import com.emergency.alert.telegram.EmergencyTelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {

    private final EmergencyTelegramBot bot;

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);
            log.info("Telegram bot registered successfully");
        } catch (Exception e) {
            log.error("Failed to register Telegram bot: {}", e.getMessage());
        }
    }
}