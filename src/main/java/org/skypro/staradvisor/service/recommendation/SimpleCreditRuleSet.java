package org.skypro.staradvisor.service.recommendation;

import org.skypro.staradvisor.model.recommendation.RecommendationDto;
import org.skypro.staradvisor.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimpleCreditRuleSet implements RecommendationRuleSet{

    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String RECOMMENDATION_TEXT = "Откройте мир выгодных кредитов с нами!\n" +
            "\n" +
            "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно!" +
            "Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n" +
            "\n" +
            "Почему выбирают нас:\n" +
            "\n" +
            "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n" +
            "\n" +
            "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n" +
            "\n" +
            "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, " +
            "лечение и многое другое.\n" +
            "\n" +
            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final RecommendationRepository recommendationRepository;

    public SimpleCreditRuleSet(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasCredit = recommendationRepository.userUsesProductType(userId, RecommendationRepository.PRODUCT_TYPE_CREDIT);
        BigDecimal debitDeposits = recommendationRepository.getSumByTransactionTypeAndProductType(
                userId,
                RecommendationRepository.TRANSACTION_TYPE_DEPOSIT,
                RecommendationRepository.PRODUCT_TYPE_DEBIT);
        BigDecimal debitWithdrawals = recommendationRepository.getSumByTransactionTypeAndProductType(
                userId,
                RecommendationRepository.TRANSACTION_TYPE_WITHDRAW,
                RecommendationRepository.PRODUCT_TYPE_DEBIT);

        if (!hasCredit && debitDeposits.compareTo(debitWithdrawals) > 0 &&
                debitWithdrawals.compareTo(new BigDecimal("100000")) > 0) {
            return Optional.of(new RecommendationDto(
                    PRODUCT_ID, PRODUCT_NAME, RECOMMENDATION_TEXT
            ));
        }
        return Optional.empty();
    }
}
