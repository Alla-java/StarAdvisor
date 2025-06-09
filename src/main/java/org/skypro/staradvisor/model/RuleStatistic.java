package org.skypro.staradvisor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Entity
public class RuleStatistic{

@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private UUID id;

private UUID ruleId;
private long count = 0;

public RuleStatistic(UUID ruleId){
    this.ruleId=ruleId;
}

public RuleStatistic() {}


public UUID getId(){
    return id;
}

public void setId(UUID id){
    this.id=id;
}

public UUID getRuleId(){
    return ruleId;
}

public void setRuleId(UUID ruleId){
    this.ruleId=ruleId;
}

public long getCount(){
    return count;
}

public void setCount(long count){
    this.count=count;
}

public void incrementCount () {
    this.count++;
}

public static RuleStatistic createForRule(UUID ruleId) {
    RuleStatistic stat = new RuleStatistic(ruleId);
    stat.setCount(1);
    return stat;
}


@Override
public boolean equals(Object o){
    if(this==o)
        return true;
    if(o==null||getClass()!=o.getClass())
        return false;
    RuleStatistic that=(RuleStatistic)o;
    return count==that.count&&Objects.equals(id,that.id)&&Objects.equals(ruleId,that.ruleId);
}

@Override
public int hashCode(){
    return Objects.hash(id,ruleId,count);
}

@Override
public String toString(){
    return "RuleStatistic{"+"id="+id+", ruleId="+ruleId+", count="+count+'}';
}

}
