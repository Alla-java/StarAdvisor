package org.skypro.staradvisor.service;

import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<RecommendationDto> evaluate(UUID userId);
}
