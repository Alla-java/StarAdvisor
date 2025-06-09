package org.skypro.staradvisor.service;

import org.skypro.staradvisor.model.RuleQuery;
import org.skypro.staradvisor.repository.RuleStatisticRepository;
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
    private final RuleStatisticRepository ruleStatisticRepository;

    public DynamicRuleService(RuleRepository ruleRepository,RuleEngine ruleEngine,RuleStatisticRepository ruleStatisticRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleEngine = ruleEngine;
        this.ruleStatisticRepository=ruleStatisticRepository;
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
    public List<RecommendationRule> getAllRules() {
        return ruleRepository.findAll();
    }

    //Метод получения списка рекомендаций на основе хранимых динамических правил из БД
    public List<RecommendationDto> getRecommendations(UUID userId) {
        return ruleRepository
                .findAll()
                .stream()
                .filter(rule -> ruleEngine.evaluate(userId, rule.getRule()))
                .peek(rule -> ruleStatisticRepository.save(rule.getId()))
                .map(rule -> new RecommendationDto(
                 rule.getId(),
                 rule.getProductName(),
                 rule.getProductText()))
                .collect(Collectors.toList());
    }
}
