package org.skypro.staradvisor.service;


import org.skypro.staradvisor.model.RecommendationDto;
import org.skypro.staradvisor.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class Invest500RuleSet implements RecommendationRuleSet {

    private static final UUID INVEST_500_PRODUCT_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String RECOMMENDATION_TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС)" +
            " от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года " +
            "и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой " +
            "портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к " +
            "финансовой независимости!";

    private final RecommendationRepository recommendationRepository;

    public Invest500RuleSet(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }


    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasDebit = recommendationRepository.userUsesProductType(userId, RecommendationRepository.PRODUCT_TYPE_DEBIT);
        boolean hasInvest = recommendationRepository.userUsesProductType(userId, RecommendationRepository.PRODUCT_TYPE_INVEST);
        BigDecimal savingDeposits = recommendationRepository.getSumByTransactionTypeAndProductType(
                userId,
                RecommendationRepository.TRANSACTION_TYPE_DEPOSIT,
                RecommendationRepository.PRODUCT_TYPE_SAVING);

        if (hasDebit && !hasInvest && savingDeposits.compareTo(new BigDecimal("1000")) > 0) {
            return Optional.of(new RecommendationDto(
                    INVEST_500_PRODUCT_ID, PRODUCT_NAME, RECOMMENDATION_TEXT
            ));
        }
        return Optional.empty();

    }
}
