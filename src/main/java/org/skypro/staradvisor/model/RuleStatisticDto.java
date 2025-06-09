package org.skypro.staradvisor.model;

import java.util.Objects;
import java.util.UUID;

public class RuleStatisticDto {
private UUID ruleId;
private long count;

public RuleStatisticDto() {
}

public RuleStatisticDto(UUID ruleId, long count) {
    this.ruleId = ruleId;
    this.count = count;
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

@Override
public boolean equals(Object o){
    if(this==o)
        return true;
    if(!(o instanceof RuleStatisticDto that))
        return false;
    return count==that.count&&Objects.equals(ruleId,that.ruleId);
}

@Override
public int hashCode(){
    return Objects.hash(ruleId,count);
}
}