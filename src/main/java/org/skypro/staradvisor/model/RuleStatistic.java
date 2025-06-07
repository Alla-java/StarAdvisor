package org.skypro.staradvisor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class RuleStatistic {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private UUID ruleId;
    private long count = 0;

    public RuleStatistic(UUID id, UUID ruleId) {
        this.id = id;
        this.ruleId = ruleId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void incrementCount() {
        this.count++;
    }
}
