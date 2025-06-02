package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.staradvisor.model.recommendation.RecommendationDto;
import org.skypro.staradvisor.model.rule.RecommendationRule;
import org.skypro.staradvisor.model.rule.RuleQuery;
import org.skypro.staradvisor.repository.RuleRepository;
import org.skypro.staradvisor.service.rules.DynamicRuleService;
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

@InjectMocks
private DynamicRuleService dynamicRuleService;

private UUID userId;
private UUID productId;
private RecommendationRule rule;

@BeforeEach
void init() {
    userId = UUID.randomUUID();
    productId = UUID.randomUUID();
    RuleQuery query = new RuleQuery("USER_OF", List.of("CREDIT"), false);
    rule = new RecommendationRule("Credit Card", productId, "Get your card", List.of(query));
}

@Test
void testCreateRule_withValidQuery_shouldSaveAndReturn() {
    when(ruleRepository.save(any(RecommendationRule.class))).thenReturn(rule);

    RecommendationRule created = dynamicRuleService.createRule(rule);

    assertNotNull(created);
    assertEquals("Credit Card", created.getProduct_name());
    verify(ruleRepository, times(1)).save(rule);
}

@Test
void testCreateRule_withInvalidQuery_shouldThrow() {
    RuleQuery badQuery = new RuleQuery("INVALID_QUERY", List.of(), false);
    RecommendationRule badRule = new RecommendationRule("Bad", UUID.randomUUID(), "Invalid", List.of(badQuery));

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
     () -> dynamicRuleService.createRule(badRule));

    assertTrue(ex.getMessage().contains("Некорректный query"));
    verify(ruleRepository, never()).save(any());
}

@Test
void testDeleteRule_shouldCallRepository() {
    UUID ruleId = UUID.randomUUID();

    dynamicRuleService.deleteRule(ruleId);

    verify(ruleRepository).deleteById(ruleId);
}

@Test
void testGetAllRules_shouldReturnList() {
    when(ruleRepository.findAll()).thenReturn(List.of(rule));

    List<RecommendationRule> rules = dynamicRuleService.getAllRules();

    assertEquals(1, rules.size());
    assertEquals(rule.getProduct_id(), rules.get(0).getProduct_id());
}

@Test
void testGetAllRules_emptyList() {
    when(ruleRepository.findAll()).thenReturn(Collections.emptyList());

    List<RecommendationRule> rules = dynamicRuleService.getAllRules();

    assertTrue(rules.isEmpty());
}

@Test
void testGetRecommendations_userMatchesRule() {
    when(ruleRepository.findAll()).thenReturn(List.of(rule));
    when(ruleEngine.evaluate(userId, rule.getRule())).thenReturn(true);

    List<RecommendationDto> recommendations = dynamicRuleService.getRecommendations(userId);

    assertEquals(1, recommendations.size());
    assertEquals(rule.getProduct_name(), recommendations.get(0).getName());
}

@Test
void testGetRecommendations_userDoesNotMatchRule() {
    when(ruleRepository.findAll()).thenReturn(List.of(rule));
    when(ruleEngine.evaluate(userId, rule.getRule())).thenReturn(false);

    List<RecommendationDto> recommendations = dynamicRuleService.getRecommendations(userId);

    assertTrue(recommendations.isEmpty());
}
}