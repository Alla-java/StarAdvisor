package org.skypro.staradvisor.service;

import org.springframework.stereotype.Service;

import java.util.*;

import org.skypro.staradvisor.repository.RuleRepository;
import org.skypro.staradvisor.model.RecommendationRule;
import org.skypro.staradvisor.model.RecommendationDto;

@Service
public class DynamicRuleService {

    private final RuleRepository ruleRepository;

    public DynamicRuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    //Метод создания динамического правила
    public RecommendationRule createRule(String productName, UUID productId, String productText, Collection<RuleQuery> rule) {
        UUID id = UUID.randomUUID(); // Генерация уникального идентификатора
        Rule newRule = new Rule(id.toString(), productName, productId, productText, rule);
        ruleRepository.save(newRule);

        return new RecommendationRule(
                id,
                newRule.getProductName(),
                newRule.getProductId().toString(),
                newRule.getProductText(),
                newRule.getRecommendationRule()
        );
    }

    //Метод удаления динамического правила
    public void deleteRule(UUID ruleId) {
        ruleRepository.deleteById(ruleId.toString());
    }

    //Метод получения списка динамических правил
    public Collection<RecommendationRule> getAllRules() {
        List<Rule> allRules = ruleRepository.findAll();
        List<RecommendationRule> recommendationRules = new ArrayList<>();

        for (Rule rule : allRules) {
            recommendationRules.add(new RecommendationRule(
                    UUID.fromString(rule.getId()), // Преобразуем строковый ID в UUID
                    rule.getProductName(),
                    rule.getProductId().toString(), // Приведение UUID к строке
                    rule.getProductText(),
                    rule.getRecommendationRule()
            ));
        }

        Map<String, Collection<RecommendationRule>> response = new HashMap<>();
        response.put("data", recommendationRules);

        return response.get("data"); // Возвращаем коллекцию внутри JSON-структуры
    }

    //Метод получения списка рекомендаций на основе хранимых динамических правил из БД
    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = new ArrayList<>();
        List<Rule> rules = ruleRepository.findAll(); // Получаем все динамические правила из БД

        RuleEngine ruleEngine = new RuleEngine(); // Создаем экземпляр движка правил

        for (Rule rule : rules) {
            boolean isApplicable = ruleEngine.evaluate(userId, rule); // Проверяем выполнение правила по userId
            if (isApplicable) {
                recommendations.add(new RecommendationDto(
                        rule.getProductId(),
                        rule.getProductName(),
                        rule.getProductText()
                ));
            }
        }

        return recommendations; // Возвращаем список применимых рекомендаций
    }
}
