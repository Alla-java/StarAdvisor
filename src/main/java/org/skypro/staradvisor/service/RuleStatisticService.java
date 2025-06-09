package org.skypro.staradvisor.service;

import org.skypro.staradvisor.model.RecommendationRule;
import org.skypro.staradvisor.model.RuleStatistic;
import org.skypro.staradvisor.model.RuleStatisticDto;
import org.skypro.staradvisor.model.RuleStatisticResponse;
import org.skypro.staradvisor.repository.RuleRepository;
import org.skypro.staradvisor.repository.RuleStatisticRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RuleStatisticService{
private final RuleStatisticRepository ruleStatisticRepository;
private final RuleRepository ruleRepository;

public RuleStatisticService(RuleStatisticRepository rulestatisticRepository,RuleRepository ruleRepository){
    this.ruleStatisticRepository=rulestatisticRepository;
    this.ruleRepository=ruleRepository;
}

public RuleStatisticResponse getStatistics(){
    List<RecommendationRule> allRules=ruleRepository.findAll();

    List<RuleStatisticDto> statDto=allRules.stream().map(rule->{
        UUID ruleId=rule.getId();
        RuleStatistic stat=ruleStatisticRepository.findByRuleId(ruleId).orElse(new RuleStatistic(ruleId));
        return new RuleStatisticDto(stat.getRuleId(),stat.getCount());
    }).collect(Collectors.toList());

    return new RuleStatisticResponse(statDto);
}
}