package org.skypro.staradvisor.service.rules;

import org.skypro.staradvisor.model.rule.RuleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.skypro.staradvisor.repository.RuleRepository;
import org.skypro.staradvisor.model.rule.RecommendationRule;
import org.skypro.staradvisor.model.recommendation.RecommendationDto;

@Service
public class DynamicRuleService {

//Допустимые значения запроса Query
private static final Set<String> VALID_QUERIES = Set.of(
 "USER_OF",
 "ACTIVE_USER_OF",
 "TRANSACTION_SUM_COMPARE",
 "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW"
);

    private final RuleRepository ruleRepository;
    private final RuleEngine ruleEngine;

@Autowired
    public DynamicRuleService(RuleRepository ruleRepository, RuleEngine ruleEngine) {
        this.ruleRepository = ruleRepository;
        this.ruleEngine = ruleEngine;
    }

    //Метод создания динамического правила
    public RecommendationRule createRule(RecommendationRule rule){
        for (RuleQuery rq : rule.getRule()) {
            if (!VALID_QUERIES.contains(rq.getQuery())) {
                throw new IllegalArgumentException("Некорректный query: " + rq.getQuery());
            }
        }
        return ruleRepository.save(rule);
    }

    //Метод удаления динамического правила
    public void deleteRule(UUID ruleId) {
        ruleRepository.deleteById(ruleId);
    }

    //Метод получения списка динамических правил
    public List<RecommendationRule> getAllRules(){
        return ruleRepository.findAll();
    }

    //Метод получения списка рекомендаций на основе хранимых динамических правил из БД
    public List<RecommendationDto> getRecommendations(UUID userId) {
        return ruleRepository.findAll().stream()
                .filter(rule -> ruleEngine.evaluate(userId, rule.getRule())) // Передаем список правил в RuleEngine
                .map(rule -> new RecommendationDto(rule.getId(), rule.getProduct_name(), rule.getProduct_text()))
                .collect(Collectors.toList());
    }
}
