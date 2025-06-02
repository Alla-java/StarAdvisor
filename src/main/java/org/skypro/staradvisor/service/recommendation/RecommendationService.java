package org.skypro.staradvisor.service.recommendation;

import org.skypro.staradvisor.model.recommendation.RecommendationDto;
import org.skypro.staradvisor.service.rules.DynamicRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Сервис для обработки и объединения рекомендаций из различных источников.
 * Комбинирует статические и динамические правила.
 */
@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> ruleSets;
    private final DynamicRuleService dynamicRuleService;

    public RecommendationService(List<RecommendationRuleSet> ruleSets, DynamicRuleService dynamicRuleService) {
        this.ruleSets = ruleSets;
        this.dynamicRuleService = dynamicRuleService;
    }

    /*
     * Получает все рекомендации для указанного пользователя.
     * Объединяет результаты статических и динамических правил.
     * @param userId Идентификатор пользователя для поиска рекомендаций
     * @return Список объектов RecommendationDto с рекомендациями
     */
    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<RecommendationDto> staticRecommendations = ruleSets.stream()
                .map(rule -> rule.evaluate(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<RecommendationDto> dynamicRecommendations = dynamicRuleService.getRecommendations(userId);

        staticRecommendations.addAll(dynamicRecommendations);
        return staticRecommendations;
    }
}
