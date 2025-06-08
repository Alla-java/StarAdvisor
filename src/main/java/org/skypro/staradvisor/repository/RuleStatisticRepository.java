package org.skypro.staradvisor.repository;

import org.skypro.staradvisor.model.RuleStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleStatisticRepository extends JpaRepository<RuleStatistic,UUID>{
Optional<RuleStatistic> findByRuleId(UUID ruleId);

default RuleStatistic save(UUID ruleId){

    Optional<RuleStatistic> existingStat=findByRuleId(ruleId);

    if(existingStat.isPresent()){
        RuleStatistic stat=existingStat.get();
        stat.incrementCount();
        return save(stat);
    } else return save(RuleStatistic.createForRule(ruleId));
}

}
