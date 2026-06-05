package com.emergency.alert.telegram;

import com.emergency.alert.entity.*;
import com.emergency.alert.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class EmergencyTelegramBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final UserResponseRepository responseRepository;
    private final NotificationRepository notificationRepository;
    private final EmergencyEventRepository eventRepository;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    private final Map<String, Boolean> waitingProfile = new ConcurrentHashMap<>();
    private final Map<String, Boolean> waitingUpdateProfile = new ConcurrentHashMap<>();

    public EmergencyTelegramBot(
            UserRepository userRepository,
            UserResponseRepository responseRepository,
            NotificationRepository notificationRepository,
            EmergencyEventRepository eventRepository
    ) {
        this.userRepository = userRepository;
        this.responseRepository = responseRepository;
        this.notificationRepository = notificationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String text = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();

        if ("/start".equals(text)) {
            send(chatId, "Команды:\n/create_profile\n/update_profile\n/delete_profile\n/status");
            return;
        }

        if ("/create_profile".equals(text)) {
            waitingProfile.put(chatId, true);
            send(chatId, "Введите: Имя, широта, долгота");
            return;
        }

        if (Boolean.TRUE.equals(waitingProfile.get(chatId))) {
            String[] parts = text.split(",");

            if (parts.length < 3) {
                send(chatId, "Неверный формат");
                return;
            }

            User user = User.builder()
                    .messengerId(chatId)
                    .fullName(parts[0].trim())
                    .latitude(Double.parseDouble(parts[1].trim()))
                    .longitude(Double.parseDouble(parts[2].trim()))
                    .build();

            userRepository.save(user);
            waitingProfile.remove(chatId);
            send(chatId, "Профиль создан");
            return;
        }

        if ("/update_profile".equals(text)) {
            waitingUpdateProfile.put(chatId, true);
            send(chatId, "Введите: Имя, широта, долгота");
            return;
        }

        if (Boolean.TRUE.equals(waitingUpdateProfile.get(chatId))) {

            User user = userRepository.findByMessengerId(chatId).orElse(null);
            if (user == null) {
                send(chatId, "Сначала создайте профиль");
                waitingUpdateProfile.remove(chatId);
                return;
            }

            String[] parts = text.split(",");

            user.setFullName(parts[0].trim());
            user.setLatitude(Double.parseDouble(parts[1].trim()));
            user.setLongitude(Double.parseDouble(parts[2].trim()));

            userRepository.save(user);
            waitingUpdateProfile.remove(chatId);
            send(chatId, "Профиль обновлён");
            return;
        }

        if ("/delete_profile".equals(text)) {
            userRepository.findByMessengerId(chatId).ifPresent(userRepository::delete);
            send(chatId, "Профиль удалён");
            return;
        }

        if ("/status".equals(text)) {
            eventRepository.findTopByOrderByCreatedAtDesc()
                    .ifPresentOrElse(
                            e -> send(chatId, e.getTitle() + "\n" + e.getStatus()),
                            () -> send(chatId, "Нет активных ЧС")
                    );
            return;
        }

        if ("Я в безопасности".equalsIgnoreCase(text)) saveResponse(chatId, "SAFE");
        if ("Нужна помощь".equalsIgnoreCase(text)) saveResponse(chatId, "HELP");
    }

    private void saveResponse(String chatId, String type) {

        User user = userRepository.findByMessengerId(chatId).orElse(null);
        if (user == null) return;

        Notification notification =
                notificationRepository.findTopByUserIdOrderBySentAtDesc(user.getId());

        if (notification == null) return;

        responseRepository.save(UserResponse.builder()
                .notificationId(notification.getId())
                .userId(user.getId())
                .responseType(type)
                .build());

        send(chatId, "Ответ сохранён");
    }

    public boolean sendEmergency(String chatId, String title, String text) {
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.setText("ЧС\n\n" + title + "\n\n" + text);
            execute(msg);
            return true;
        } catch (Exception e) {
            log.error("Telegram error: {}", e.getMessage());
            return false;
        }
    }

    private void send(String chatId, String text) {
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.setText(text);
            execute(msg);
        } catch (Exception e) {
            log.error("Telegram error: {}", e.getMessage());
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