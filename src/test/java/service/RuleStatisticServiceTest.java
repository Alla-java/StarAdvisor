package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.staradvisor.model.RecommendationRule;
import org.skypro.staradvisor.model.RuleStatistic;
import org.skypro.staradvisor.model.RuleStatisticDto;
import org.skypro.staradvisor.model.RuleStatisticResponse;
import org.skypro.staradvisor.repository.RuleRepository;
import org.skypro.staradvisor.repository.RuleStatisticRepository;
import org.skypro.staradvisor.service.RuleStatisticService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleStatisticServiceTest {

    @Mock
    private RuleStatisticRepository statisticRepository;

    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private RuleStatisticService ruleStatisticService;

    private UUID ruleId;

    @Test
    void shouldReturnAllRuleStatisticsWithMissingCountsAsZero() {
        UUID rule1Id = UUID.randomUUID();
        UUID rule2Id = UUID.randomUUID();

        RecommendationRule rule1 = new RecommendationRule("Продукт 1", rule1Id, "Текст", List.of());
        RecommendationRule rule2 = new RecommendationRule("Продукт 2", rule2Id, "Описание", List.of());
        rule1.setId(rule1Id);
        rule2.setId(rule2Id);

        when(ruleRepository.findAll()).thenReturn(List.of(rule1, rule2));
        when(statisticRepository.findById(rule1Id))
                .thenReturn(Optional.of(new RuleStatistic(rule1Id)));
        when(statisticRepository.findById(rule2Id))
                .thenReturn(Optional.empty());

        RuleStatisticResponse result = ruleStatisticService.getStatistics();

        assertEquals(2, result.getStats().size());

        RuleStatisticDto statDto1 = result.getStats().stream()
                .filter(statDto -> statDto.getRuleId().equals(rule1Id))
                .findFirst().orElseThrow();
        RuleStatisticDto statDto2 = result.getStats().stream()
                .filter(statDto -> statDto.getRuleId().equals(rule2Id))
                .findFirst().orElseThrow();

        assertEquals(0, statDto1.getCount());
        assertEquals(0, statDto2.getCount());
    }


}
