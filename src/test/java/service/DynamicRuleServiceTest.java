package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.staradvisor.model.RecommendationDto;
import org.skypro.staradvisor.model.RecommendationRule;
import org.skypro.staradvisor.model.RuleQuery;
import org.skypro.staradvisor.repository.RuleRepository;
import org.skypro.staradvisor.repository.RuleStatisticRepository;
import org.skypro.staradvisor.service.DynamicRuleService;
import org.skypro.staradvisor.service.rules.RuleEngine;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DynamicRuleServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private RuleEngine ruleEngine;

    @Mock
    private RuleStatisticRepository ruleStatisticRepository;

    @InjectMocks
    private DynamicRuleService dynamicRuleService;

    @BeforeEach
    void setUp() {
        ruleRepository = mock(RuleRepository.class);
        ruleEngine = mock(RuleEngine.class);
        dynamicRuleService = new DynamicRuleService(ruleRepository,ruleEngine,ruleStatisticRepository);
    }

    @Test //создание правила
    void testCreateRule_Success() {
        UUID productId = UUID.randomUUID();
        UUID ruleId = UUID.randomUUID();
        List<RuleQuery> rules = List.of(new RuleQuery("USER_OF", List.of("CREDIT"), false));

        RecommendationRule expectedRule = new RecommendationRule("Product A", productId, "Test Description", rules);

        when(ruleRepository.save(any(RecommendationRule.class))).thenReturn(expectedRule);

        RecommendationRule actualRule = dynamicRuleService.createRule("Product A", productId, "Test Description", rules);

        assertNotNull(actualRule);
        assertEquals("Product A", actualRule.getProductName());
        assertEquals(productId, actualRule.getProductId());
    }

    @Test //удаление правила
    void testDeleteRule_Success() {
        UUID ruleId = UUID.randomUUID();

        dynamicRuleService.deleteRule(ruleId);

        verify(ruleRepository, times(1)).deleteById(ruleId);
    }

    @Test //получение всех существующих динамических правил
    void testGetAllRules_Success() {
        UUID ruleId = UUID.randomUUID();
        List<RecommendationRule> mockRules = List.of(new RecommendationRule("Test Product", ruleId, "Test Text", new ArrayList<>()));

        when(ruleRepository.findAll()).thenReturn(mockRules);

        List<RecommendationRule> response = dynamicRuleService.getAllRules();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test Product", response.get(0).getProductName());
    }

    @Test //получение всех существующих динамических правил с null в ответе
    void testGetAllRules_EmptyList() {
        when(ruleRepository.findAll()).thenReturn(new ArrayList<>());

        List<RecommendationRule> response = dynamicRuleService.getAllRules();

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
    @Test //получение динамических правил для одного пользователя
    void testGetRecommendations_Success() {
        UUID userId = UUID.randomUUID();
        UUID ruleId = UUID.randomUUID();
        RecommendationRule rule = new RecommendationRule("Loan", ruleId, "Sample Text", new ArrayList<>());

        when(ruleRepository.findAll()).thenReturn(List.of(rule));
        when(ruleEngine.evaluate(userId, rule.getRule())).thenReturn(true);

        List<RecommendationDto> recommendations = dynamicRuleService.getRecommendations(userId);

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        assertEquals(1, recommendations.size());
    }

    @Test //получение динамических правил для одного пользователя
    void testGetRecommendations_UserNotMatchingRules_ShouldReturnEmpty() {
        UUID userId = UUID.randomUUID();
        UUID ruleId = UUID.randomUUID();
        RecommendationRule rule = new RecommendationRule("Loan", ruleId, "Sample Text", new ArrayList<>());

        when(ruleRepository.findAll()).thenReturn(List.of(rule));
        when(ruleEngine.evaluate(userId, rule.getRule())).thenReturn(false);

        List<RecommendationDto> recommendations = dynamicRuleService.getRecommendations(userId);

        assertNotNull(recommendations);
        assertTrue(recommendations.isEmpty());
    }
}




