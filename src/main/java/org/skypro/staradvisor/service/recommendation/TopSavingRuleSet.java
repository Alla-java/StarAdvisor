package org.skypro.staradvisor.service.recommendation;

import org.skypro.staradvisor.model.recommendation.RecommendationDto;
import org.skypro.staradvisor.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String RECOMMENDATION_TEXT =
            "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, " +
            "который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n" +
            "\n" +
            "Преимущества «Копилки»:\n" +
            "\n" +
            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n" +
            "\n" +
            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n" +
            "\n" +
            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n" +
            "\n" +
            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final RecommendationRepository recommendationRepository;

    public TopSavingRuleSet(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasDebit = recommendationRepository.userUsesProductType(userId, RecommendationRepository.PRODUCT_TYPE_DEBIT);
        BigDecimal debitDeposits = recommendationRepository.getSumByTransactionTypeAndProductType(
                userId,
                RecommendationRepository.TRANSACTION_TYPE_DEPOSIT,
                RecommendationRepository.PRODUCT_TYPE_DEBIT);
        BigDecimal savingDeposits = recommendationRepository.getSumByTransactionTypeAndProductType(
                userId,
                RecommendationRepository.TRANSACTION_TYPE_DEPOSIT,
                RecommendationRepository.PRODUCT_TYPE_SAVING);
        BigDecimal debitWithdrawals = recommendationRepository.getSumByTransactionTypeAndProductType(
                userId,
                RecommendationRepository.TRANSACTION_TYPE_WITHDRAW,
                RecommendationRepository.PRODUCT_TYPE_DEBIT);

        boolean condition1 = debitDeposits.compareTo(new BigDecimal("50000")) >= 0 ||
                savingDeposits.compareTo(new BigDecimal("50000")) >= 0;
        boolean condition2 = debitDeposits.compareTo(debitWithdrawals) > 0;

        if (hasDebit && condition1 && condition2) {
            return Optional.of(new RecommendationDto(
                    PRODUCT_ID, PRODUCT_NAME, RECOMMENDATION_TEXT
            ));
        }
        return Optional.empty();
    }
}
