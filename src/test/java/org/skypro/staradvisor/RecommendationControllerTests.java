package org.skypro.staradvisor;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.skypro.staradvisor.controller.RecommendationController;
import org.skypro.staradvisor.model.RecommendationDto;
import org.skypro.staradvisor.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RecommendationController.class)
public class RecommendationControllerTests{

@Autowired
private MockMvc mockMvc;

@MockitoBean
private RecommendationService recommendationsService;

@Test
void getExistRecommendations_thenReturnRecommendationsJson() throws Exception{

    UUID userId=UUID.randomUUID();
    UUID firstRecommendationId=UUID.randomUUID();
    UUID secondRecommendationId=UUID.randomUUID();

    List<RecommendationDto> recommendations=List.of(new RecommendationDto(firstRecommendationId,"Продукт A","Описание A"),new RecommendationDto(secondRecommendationId,"Продукт В","Описание B"));

    when(recommendationsService.getRecommendations(userId)).thenReturn(recommendations);

    mockMvc.perform(get("/recommendation/"+userId))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.user_id").value(userId.toString()))
     .andExpect(jsonPath("$.recommendations").isArray())
     .andExpect(jsonPath("$.recommendations.length()").value(2))
     .andExpect(jsonPath("$.recommendations[0].name").value("Продукт A"))
     .andExpect(jsonPath("$.recommendations[1].id").value(secondRecommendationId.toString()));
}


}
