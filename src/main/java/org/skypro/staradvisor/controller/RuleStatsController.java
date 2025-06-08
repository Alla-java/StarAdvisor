package org.skypro.staradvisor.controller;


import org.skypro.staradvisor.model.RuleStatisticResponse;
import org.skypro.staradvisor.service.RuleStatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rule/stats")
public class RuleStatsController{

private final RuleStatisticService ruleStatisticService;

public RuleStatsController(RuleStatisticService ruleStatisticService){
    this.ruleStatisticService=ruleStatisticService;
}

@GetMapping
public RuleStatisticResponse getStatistics(){
    return ruleStatisticService.getStatistics();
}
}