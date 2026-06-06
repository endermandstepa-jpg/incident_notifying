package com.emergency.alert.telegram;

import com.emergency.alert.entity.User;
import com.emergency.alert.entity.UserResponse;
import com.emergency.alert.storage.InMemoryDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class EmergencyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    private final ConcurrentHashMap<String, Boolean> waitingProfile = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> waitingUpdateProfile = new ConcurrentHashMap<>();

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (!update.hasMessage() || !update.getMessage().hasText()) {
                return;
            }

            String text = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();

            // ---------------- START ----------------
            if ("/start".equalsIgnoreCase(text)) {
                send(chatId,
                        "Команды:\n" +
                        "/create_profile\n" +
                        "/update_profile\n" +
                        "/delete_profile\n" +
                        "/status");
                return;
            }

            // ---------------- CREATE ----------------
            if ("/create_profile".equalsIgnoreCase(text)) {
                waitingProfile.put(chatId, true);
                send(chatId, "Введите: Имя, широта, долгота");
                return;
            }

            if (Boolean.TRUE.equals(waitingProfile.get(chatId))) {
                String[] parts = text.split(",");

                if (parts.length < 3) {
                    send(chatId, "Неверный формат. Нужно: Имя, широта, долгота");
                    return;
                }

                try {
                    User user = User.builder()
                            .id(InMemoryDatabase.USER_SEQ.getAndIncrement())
                            .messengerId(chatId)
                            .fullName(parts[0].trim())
                            .latitude(Double.parseDouble(parts[1].trim()))
                            .longitude(Double.parseDouble(parts[2].trim()))
                            .build();

                    InMemoryDatabase.USERS.put(user.getId(), user);

                    waitingProfile.remove(chatId);
                    send(chatId, "Пользователь создан");

                } catch (Exception e) {
                    log.error("CREATE PROFILE FAILED", e);
                    send(chatId, "Ошибка создания профиля");
                }
                return;
            }

            // ---------------- UPDATE ----------------
            if ("/update_profile".equalsIgnoreCase(text)) {
                waitingUpdateProfile.put(chatId, true);
                send(chatId, "Введите: Имя, широта, долгота");
                return;
            }

            if (Boolean.TRUE.equals(waitingUpdateProfile.get(chatId))) {

                User user = InMemoryDatabase.USERS.values()
                        .stream()
                        .filter(u -> chatId.equals(u.getMessengerId()))
                        .findFirst()
                        .orElse(null);

                if (user == null) {
                    send(chatId, "Сначала создайте профиль");
                    waitingUpdateProfile.remove(chatId);
                    return;
                }

                String[] parts = text.split(",");

                if (parts.length < 3) {
                    send(chatId, "Неверный формат");
                    return;
                }

                try {
                    user.setFullName(parts[0].trim());
                    user.setLatitude(Double.parseDouble(parts[1].trim()));
                    user.setLongitude(Double.parseDouble(parts[2].trim()));

                    InMemoryDatabase.USERS.put(user.getId(), user);

                    send(chatId, "Профиль обновлён");

                } catch (Exception e) {
                    log.error("UPDATE PROFILE FAILED", e);
                    send(chatId, "Ошибка обновления");
                }

                waitingUpdateProfile.remove(chatId);
                return;
            }

            // ---------------- DELETE ----------------
            if ("/delete_profile".equalsIgnoreCase(text)) {

                User user = InMemoryDatabase.USERS.values()
                        .stream()
                        .filter(u -> chatId.equals(u.getMessengerId()))
                        .findFirst()
                        .orElse(null);

                if (user != null) {
                    InMemoryDatabase.USERS.remove(user.getId());
                }

                send(chatId, "Профиль удален");
                return;
            }

            // ---------------- STATUS ----------------
            if ("/status".equalsIgnoreCase(text)) {
                send(chatId, "Сервис работает. Активных событий: " + InMemoryDatabase.EVENTS.size());
                return;
            }

            // ---------------- RESPONSE SAFE ----------------
            if ("Я в безопасности".equalsIgnoreCase(text) ||
                "Нужна помощь".equalsIgnoreCase(text)) {

                saveResponse(chatId, text);
            }

        } catch (Exception e) {
            log.error("TELEGRAM UPDATE ERROR", e);
        }
    }

    private void saveResponse(String chatId, String type) {
        try {
            User user = InMemoryDatabase.USERS.values()
                    .stream()
                    .filter(u -> chatId.equals(u.getMessengerId()))
                    .findFirst()
                    .orElse(null);

            if (user == null) return;

            UserResponse response = UserResponse.builder()
                    .id(InMemoryDatabase.RESP_SEQ.getAndIncrement())
                    .userId(user.getId())
                    .responseType(type)
                    .build();

            InMemoryDatabase.RESPONSES.put(response.getId(), response);

            send(chatId, "Ответ сохранён");

        } catch (Exception e) {
            log.error("SAVE RESPONSE FAILED", e);
        }
    }

    public boolean sendEmergency(String chatId, String title, String text) {
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.setText(
                    "🚨 ЧС 🚨\n\n" +
                    title + "\n\n" +
                    text + "\n\n" +
                    "Ответьте:\n" +
                    "Я в безопасности\n" +
                    "Нужна помощь"
            );

            execute(msg);
            return true;

        } catch (Exception e) {
            log.error("TELEGRAM SEND FAILED", e);
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
            log.error("SEND MESSAGE FAILED", e);
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