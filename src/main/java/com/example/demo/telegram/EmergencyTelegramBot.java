
package com.emergency.alert.telegram;

import com.emergency.alert.entity.*;
import com.emergency.alert.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmergencyTelegramBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final UserResponseRepository responseRepository;
    private final NotificationRepository notificationRepository;
    private final EmergencyEventRepository eventRepository;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) {
            return;
        }

        if (!update.getMessage().hasText()) {
            return;
        }

        String text =
                update.getMessage().getText();

        String chatId =
                update.getMessage()
                        .getChatId()
                        .toString();

        if ("/start".equals(text)) {

            send(chatId,
                    """
                    Команды:
                    /create_profile
                    /delete_profile
                    /status
                    """);

            return;
        }

        if ("/create_profile".equals(text)) {

            Optional<User> existing =
                    userRepository.findByMessengerId(chatId);

            if (existing.isPresent()) {

                send(chatId,
                        "Профиль уже существует");

                return;
            }

            User user =
                    User.builder()
                            .messengerId(chatId)
                            .fullName(
                                    update.getMessage()
                                            .getFrom()
                                            .getFirstName())
                            .city("Unknown")
                            .latitude(0.0)
                            .longitude(0.0)
                            .createdAt(Instant.now())
                            .build();

            userRepository.save(user);

            send(chatId,
                    "Профиль создан");

            return;
        }

        if ("/delete_profile".equals(text)) {

            userRepository.findByMessengerId(chatId)
                    .ifPresent(userRepository::delete);

            send(chatId,
                    "Профиль удален");

            return;
        }

        if ("/status".equals(text)) {

            eventRepository
                    .findTopByOrderByCreatedAtDesc()
                    .ifPresentOrElse(
                            event -> send(
                                    chatId,
                                    event.getTitle()
                                            + "\n"
                                            + event.getStatus()),
                            () -> send(
                                    chatId,
                                    "Нет активных ЧС"));

            return;
        }

        if ("SAFE".equalsIgnoreCase(text)) {
            saveResponse(chatId, "SAFE");
        }

        if ("HELP".equalsIgnoreCase(text)) {
            saveResponse(chatId, "HELP");
        }
    }

    private void saveResponse(
            String chatId,
            String responseType
    ) {

        Optional<User> userOpt =
                userRepository.findByMessengerId(chatId);

        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();

        Notification notification =
                notificationRepository
                        .findTopByUserIdOrderBySentAtDesc(
                                user.getUserId());

        if (notification == null) {
            return;
        }

        responseRepository.save(
                UserResponse.builder()
                        .notificationId(
                                notification.getNotificationId())
                        .userId(user.getUserId())
                        .responseType(responseType)
                        .responseTime(Instant.now())
                        .build()
        );

        send(chatId,
                "Ответ сохранен: "
                        + responseType);
    }

    public boolean sendEmergency(
            String chatId,
            String title,
            String text
    ) {

        try {

            SendMessage message =
                    new SendMessage();

            message.setChatId(chatId);

            message.setText(
                    "ЧС\n\n"
                            + title
                            + "\n\n"
                            + text
                            + "\n\n"
                            + "Ответьте SAFE или HELP");

            execute(message);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private void send(
            String chatId,
            String text
    ) {

        try {

            SendMessage message =
                    new SendMessage();

            message.setChatId(chatId);
            message.setText(text);

            execute(message);

        } catch (Exception ignored) {
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}