package org.skypro.staradvisor.controller;

import org.skypro.staradvisor.model.RecommendationResponse;
import org.skypro.staradvisor.model.RecommendationDTO;
import org.skypro.staradvisor.service.RecommendationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController{

private final RecommendationsService recommendationService;

public RecommendationController(RecommendationsService recommedationService){
    this.recommendationService=recommedationService;

}

@GetMapping("/{userId}")
public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId){
    List<RecommendationDTO> recommendations=recommendationService.getRecommendations(userId);
    RecommendationResponse response=new RecommendationResponse(userId,recommendations);
    return ResponseEntity.ok(response);
}

@GetMapping("/{userId}/random-amount")
public ResponseEntity<Map<String,Object>> getRandomAmount(@PathVariable UUID userId){
    int amount=recommendationService.getRandomTransactionAmount(userId);
    Map<String,Object> response=Map.of("user_id",userId,"amount",amount);
    return ResponseEntity.ok(response);
}

}
