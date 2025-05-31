package org.skypro.staradvisor.service;

import org.skypro.staradvisor.model.RuleQuery;
import org.skypro.staradvisor.service.rules.RuleEngine;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.skypro.staradvisor.repository.RuleRepository;
import org.skypro.staradvisor.model.RecommendationRule;
import org.skypro.staradvisor.model.RecommendationDto;

@Service
public class DynamicRuleService {

    private final RuleRepository ruleRepository;
    private final RuleEngine ruleEngine;

    public DynamicRuleService(RuleRepository ruleRepository, RuleEngine ruleEngine) {
        this.ruleRepository = ruleRepository;
        this.ruleEngine = ruleEngine;
    }

    //Метод создания динамического правила
    public RecommendationRule createRule(String productName, UUID productId, String productText, List<RuleQuery> rules) {
        RecommendationRule newRule = new RecommendationRule(productName, productId, productText, rules);
        ruleRepository.save(newRule);
        return newRule;
    }

    //Метод удаления динамического правила
    public void deleteRule(UUID ruleId) {
        ruleRepository.deleteById(ruleId);
    }

    //Метод получения списка динамических правил
    public Map<String, List<RecommendationRule>> getAllRules() {
        List<RecommendationRule> rules = ruleRepository.findAll();
        Map<String, List<RecommendationRule>> response = new HashMap<>();
        response.put("data", rules);
        return response;
    }

    //Метод получения списка рекомендаций на основе хранимых динамических правил из БД
    public List<RecommendationDto> getRecommendations(UUID userId) {
        return ruleRepository.findAll().stream()
                .filter(rule -> ruleEngine.evaluate(userId, rule.getRule())) // Передаем список правил в RuleEngine
                .map(rule -> new RecommendationDto(rule.getId(), rule.getProductName(), rule.getProductText()))
                .collect(Collectors.toList());
    }
}
