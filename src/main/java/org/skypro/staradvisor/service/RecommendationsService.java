package org.skypro.staradvisor.service;

import org.skypro.staradvisor.model.RecommendationDTO;
import org.skypro.staradvisor.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationsService{
private final RecommendationsRepository recommendationsRepository;

public RecommendationsService(RecommendationsRepository recommendationsRepository){
    this.recommendationsRepository=recommendationsRepository;
}

public int getRandomTransactionAmount(UUID userId){
    return recommendationsRepository.getRandomTransactionAmount(userId);
}

public List<RecommendationDTO> getRecommendations(UUID userId){
    return Collections.emptyList();
}

}
