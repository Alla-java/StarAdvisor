package org.skypro.staradvisor.model.rule;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class RecommendationRule{
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private UUID id;

private String product_name;
private UUID product_id;
private String product_text;

@Convert(converter=RuleQueryListConverter.class)
private List<RuleQuery> rule;

public RecommendationRule(){
}

public RecommendationRule(String product_name,UUID product_id,String product_text,List<RuleQuery> rule){
    this.product_name=product_name;
    this.product_id=product_id;
    this.product_text=product_text;
    this.rule=rule;
}

public UUID getId(){
    return id;
}

public void setId(UUID id){
    this.id=id;
}

public String getProduct_name(){
    return product_name;
}

public void setProduct_name(String product_name){
    this.product_name=product_name;
}

public UUID getProduct_id(){
    return product_id;
}

public void setProduct_id(UUID product_id){
    this.product_id=product_id;
}

public String getProduct_text(){
    return product_text;
}

public void setProduct_text(String product_text){
    this.product_text=product_text;
}

public List<RuleQuery> getRule(){
    return rule;
}

public void setRule(List<RuleQuery> rule){
    this.rule=rule;
}

@Override
public boolean equals(Object o){
    if(this==o)
        return true;
    if(o==null||getClass()!=o.getClass())
        return false;
    RecommendationRule recommendationRule=(RecommendationRule)o;
    return Objects.equals(id,recommendationRule.id)&&Objects.equals(product_name,recommendationRule.product_name)&&Objects.equals(product_id,recommendationRule.product_id)&&Objects.equals(product_text,recommendationRule.product_text)&&Objects.equals(rule,recommendationRule.rule);
}

@Override
public int hashCode(){
    return Objects.hash(id,product_name,product_id,product_text,rule);
}

@Override
public String toString(){
    return "RecommendationRule{"+"id="+id+", product_name='"+product_name+'\''+", product_id="+product_id+", product_text='"+product_text+'\''+", rule="+rule+'}';
}
}
