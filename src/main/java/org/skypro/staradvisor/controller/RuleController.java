package org.skypro.staradvisor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.skypro.staradvisor.service.DynamicRuleService;
import org.skypro.staradvisor.model.RecommendationRule;
import org.skypro.staradvisor.model.RuleQuery;

import java.util.List;
import java.util.UUID;

/*
* Контроллер для управления динамическими правилами
* (Создание правила, удаление правила, получение списка всех правил)
*/

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final DynamicRuleService dynamicRuleService;

    public RuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    /*
    * Создание нового правила рекомендаций
    * @param productName Название продукта (параметр запроса)
    * @param productId UUID продукта (параметр запроса)
    * @param productText Текст рекомендации (параметр запроса)
    * @param rule Список условий правила (тело запроса в формате JSON)
    * @return ResponseEntity с созданным правилом и статусом 200 OK
    */
    @PostMapping
    public ResponseEntity<RecommendationRule> createRule(@RequestParam String productName,
                                                         @RequestParam UUID uuid,
                                                         @RequestParam String productText,
                                                         @RequestBody List<RuleQuery> rule) {
        RecommendationRule createdRule = dynamicRuleService.createRule(productName, uuid, productText, rule);
        return ResponseEntity.ok(createdRule);
    }

    /*
     * Удаление правила по его идентификатору
     * @param id UUID правила для удаления (из пути URL)
     * @return ResponseEntity с пустым телом и статусом 204 No Content
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID uuid) {
        dynamicRuleService.deleteRule(uuid);
        return ResponseEntity.noContent().build();
    }

    /*
     * Получение списка всех существующих правил рекомендаций
     * @return ResponseEntity со списком всех правил и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<List<RecommendationRule>> getAllRules () {
        List<RecommendationRule> rules = dynamicRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }


}
