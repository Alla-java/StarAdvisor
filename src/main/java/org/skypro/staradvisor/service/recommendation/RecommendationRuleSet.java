package org.skypro.staradvisor.service.recommendation;

import org.skypro.staradvisor.model.recommendation.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<RecommendationDto> evaluate(UUID userId);
}
