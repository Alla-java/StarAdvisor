package org.skypro.staradvisor.service;

import org.skypro.staradvisor.model.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<RecommendationDto> evaluate(UUID userId);
}
