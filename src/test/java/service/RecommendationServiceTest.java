package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.staradvisor.model.RecommendationDto;
import org.skypro.staradvisor.service.RecommendationRuleSet;
import org.skypro.staradvisor.service.RecommendationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private RecommendationRuleSet ruleSet1;

    @Mock
    private RecommendationRuleSet ruleSet2;

    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendationService(List.of(ruleSet1, ruleSet2));
    }

    @Test
    void shouldReturnOnlyNonEmptyRecommendations() {
        UUID userId = UUID.randomUUID();
        RecommendationDto expectedDto = new RecommendationDto(
                UUID.randomUUID(),
                "Invest 500",
                "Описание"
        );

        when(ruleSet1.evaluate(userId)).thenReturn(Optional.of(expectedDto));

        // ruleSet2 — не подходит, возвращает пусто
        when(ruleSet2.evaluate(userId)).thenReturn(Optional.empty());

        List<RecommendationDto> result = recommendationService.getRecommendations(userId);

        assertEquals(1, result.size());
        assertEquals(expectedDto, result.get(0));

        // Убедимся, что оба правила вызывались
        verify(ruleSet1).evaluate(userId);
        verify(ruleSet2).evaluate(userId);
    }

    @Test
    void shouldReturnEmptyListWhenNoRulesApply() {
        UUID userId = UUID.randomUUID();

        when(ruleSet1.evaluate(userId)).thenReturn(Optional.empty());
        when(ruleSet2.evaluate(userId)).thenReturn(Optional.empty());

        List<RecommendationDto> result = recommendationService.getRecommendations(userId);

        assertTrue(result.isEmpty());
        verify(ruleSet1).evaluate(userId);
        verify(ruleSet2).evaluate(userId);
    }
}
