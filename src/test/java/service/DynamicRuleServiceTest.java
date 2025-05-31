package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.skypro.staradvisor.model.RecommendationDto;
import org.skypro.staradvisor.service.DynamicRuleService;
import org.springframework.boot.test.context.SpringBootTest;

import org.skypro.staradvisor.repository.rulerepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DynamicRuleServiceTest {

    private RuleRepository ruleRepository;
    private RuleEngine ruleEngine;
    private DynamicRuleService dynamicRuleService;

    @BeforeEach
    void setUp() {
        ruleRepository = mock(RuleRepository.class);
        ruleEngine = mock(RuleEngine.class);
        dynamicRuleService = new DynamicRuleService(ruleRepository);
    }

    @Test //создание правила
    void testCreateRule() {
        UUID productId = UUID.randomUUID();
        String productName = "Простой кредит";
        String productText = "Текст рекомендации";
        Collection<RuleQuery> ruleQueries = List.of(new RuleQuery("USER_OF", List.of("CREDIT"), true));

        RecommendationRule recommendationRule = dynamicRuleService.createRule(productName, productId, productText, ruleQueries);

        assertNotNull(recommendationRule);
        assertEquals(productName, recommendationRule.getProductName());
        assertEquals(productId.toString(), recommendationRule.getProductId());
        assertEquals(productText, recommendationRule.getProductText());
        assertEquals(ruleQueries, recommendationRule.getRecommendationRule());

        verify(ruleRepository, times(1)).save(any(Rule.class));
    }

    @Test //удаление правила
    void testDeleteRule() {
        UUID ruleId = UUID.randomUUID();

        dynamicRuleService.deleteRule(ruleId);

        verify(ruleRepository, times(1)).deleteById(ruleId.toString());
    }

    @Test //получение всех динамических правил
    void testGetAllRules() {
        Rule rule = new Rule(UUID.randomUUID().toString(), "Простой кредит", UUID.randomUUID(), "Текст рекомендации", List.of(new RuleQuery("USER_OF", List.of("CREDIT"), true)));
        when(ruleRepository.findAll()).thenReturn(List.of(rule));

        Collection<RecommendationRule> recommendationRules = dynamicRuleService.getAllRules();

        assertNotNull(recommendationRules);
        assertEquals(1, recommendationRules.size());

        RecommendationRule recommendationRule = recommendationRules.iterator().next();
        assertEquals(rule.getProductName(), recommendationRule.getProductName());
        assertEquals(rule.getProductId().toString(), recommendationRule.getProductId());
        assertEquals(rule.getProductText(), recommendationRule.getProductText());

        verify(ruleRepository, times(1)).findAll();
    }

    @Test //получение рекомендаций для определенного пользователя
    void testGetRecommendations() {
        Rule rule = new Rule(UUID.randomUUID().toString(), "Простой кредит", UUID.randomUUID(), "Текст рекомендации", List.of(new RuleQuery("USER_OF", List.of("CREDIT"), true)));
        when(ruleRepository.findAll()).thenReturn(List.of(rule));
        UUID userId = UUID.randomUUID();
        when(ruleEngine.evaluate(userId, rule)).thenReturn(true);

        List<RecommendationDto> recommendations = dynamicRuleService.getRecommendations(userId);

        assertNotNull(recommendations);
        assertEquals(1, recommendations.size());

        RecommendationDto recommendationDto = recommendations.get(0);
        assertEquals(rule.getProductName(), recommendationDto.getName());
        assertEquals(rule.getProductId(), recommendationDto.getId());
        assertEquals(rule.getProductText(), recommendationDto.getText());

        verify(ruleRepository, times(1)).findAll();
        verify(ruleEngine, times(1)).evaluate(userId, rule);
    }

    @Test //Проверяет, что метод createRule выбрасывает NullPointerException, если все входные параметры null
    void testCreateRuleWithNullValues() {
        assertThrows(NullPointerException.class, () -> {
            dynamicRuleService.createRule(null, null, null, null);
        });
    }

    @Test //Проверяет, что getAllRules() корректно обрабатывает случай, когда в базе нет правил, и возвращает пустой список
    void testGetAllRulesWhenNoRulesExist() {
        when(ruleRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<RecommendationRule> recommendationRules = dynamicRuleService.getAllRules();

        assertNotNull(recommendationRules);
        assertTrue(recommendationRules.isEmpty());

        verify(ruleRepository, times(1)).findAll();
    }

    @Test //Проверяет, что getRecommendations(null) выбрасывает NullPointerException
    void testGetRecommendationsWithNullUserId() {
        assertThrows(NullPointerException.class, () -> {
            dynamicRuleService.getRecommendations(null);
        });
    }
}




