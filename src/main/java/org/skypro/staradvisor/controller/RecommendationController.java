package org.skypro.staradvisor.controller;

import org.skypro.staradvisor.model.recommendation.RecommendationResponse;
import org.skypro.staradvisor.model.recommendation.RecommendationDto;
import org.skypro.staradvisor.service.recommendation.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController{

private final RecommendationService recommendationService;

public RecommendationController(RecommendationService recommedationService){
    this.recommendationService=recommedationService;

}

@GetMapping("/{userId}")
public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId){
    List<RecommendationDto> recommendations=recommendationService.getRecommendations(userId);
    RecommendationResponse response=new RecommendationResponse(userId,recommendations);
    return ResponseEntity.ok(response);
}
}
