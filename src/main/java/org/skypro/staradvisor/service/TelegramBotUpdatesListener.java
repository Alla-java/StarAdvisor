package org.skypro.staradvisor.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import jakarta.annotation.PostConstruct;
import org.skypro.staradvisor.model.RecommendationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.skypro.staradvisor.repository.RecommendationRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service //он слушает апдейты, и вызывает telegramBot.setUpdatesListener(this)
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final RecommendationService recommendationService;
    private final RecommendationRepository recommendationRepository;

    private static final Pattern RECOMMEND_COMMAND_PATTERN = Pattern.compile("^/recommend\\s+(\\S+)$");

    @Autowired
    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      RecommendationService recommendationService,
                                      RecommendationRepository recommendationRepository) {
        this.telegramBot = telegramBot;
        this.recommendationService = recommendationService;
        this.recommendationRepository = recommendationRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            logger.info("Received update: {}", update);

            Message msg = update.message(); //
            if (msg == null || msg.text() == null) continue;

            String messageText = msg.text().trim();
            Long chatId = msg.chat().id();

            if (messageText.equals("/start")) { // Отправляется приветственное сообщение
                String welcomeText = """
                Привет! Я бот StarAdvisor ⭐
                Я порекомендую тебе самые лучшие банковские продукты!

                Используй команду:
                /recommend username
                """;
                telegramBot.execute(new SendMessage(chatId, welcomeText));
                continue;
            }

            Matcher matcher = RECOMMEND_COMMAND_PATTERN.matcher(messageText);
            if (matcher.matches()) {
                String username = matcher.group(1);

                //получение userId по username
                Optional<UUID> userIdOpt = recommendationRepository.findUserIdByUsername(username);
                if (userIdOpt.isEmpty()) {
                    telegramBot.execute(new SendMessage(chatId, "Пользователь не найден ❌"));
                    continue;
                }

                //получение полного имени пользователя
                UUID userId = userIdOpt.get();
                String fullName = recommendationRepository.getUserFullName(userId);

                //получение рекомендаций для этого пользователя по userId
                List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);

                //если рекомендаций нет, то отправляем это сообщение
                if (recommendations.isEmpty()) {
                    telegramBot.execute(new SendMessage(chatId,
                            String.format("Здравствуйте, %s!\nНа данный момент у нас нет новых рекомендаций.", fullName)));
                } else { //если есть рекомендации
                    String recList = recommendations.stream()
                            .map(r -> "• " + r.getName() + " — " + r.getText())
                            .collect(Collectors.joining("\n"));

                    String response = String.format("""
                    Здравствуйте, %s!

                    Новые продукты для вас:
                    %s
                    """, fullName, recList);
                    //то отправляем найденные рекомендации в телеграм
                    telegramBot.execute(new SendMessage(chatId, response));
                }
                continue;
            }

            telegramBot.execute(new SendMessage(chatId,
                    "Неизвестная команда. Используйте /start для справки."));
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
