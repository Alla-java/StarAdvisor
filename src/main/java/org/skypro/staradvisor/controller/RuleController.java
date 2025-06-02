package org.skypro.staradvisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.skypro.staradvisor.service.rules.DynamicRuleService;
import org.skypro.staradvisor.model.rule.RecommendationRule;

import java.util.Map;
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

    //Создание нового правила рекомендаций
    @PostMapping
    public ResponseEntity<?> createRule(@RequestBody RecommendationRule rule){
        try{
            RecommendationRule createdRule=dynamicRuleService.createRule(rule);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неправильный формат правила: "+e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера "+e.getMessage());
        }
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
    public ResponseEntity<?> getAllRules(){
        return ResponseEntity.ok(Map.of("data",dynamicRuleService.getAllRules()));
    }


}
