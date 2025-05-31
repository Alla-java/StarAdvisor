package org.skypro.staradvisor.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class RecommendationRule{
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private UUID id;

private String productName;
private UUID productId;
private String productText;

@Convert(converter=RuleQueryListConverter.class)
private List<RuleQuery> rule;

public RecommendationRule(){
}

public RecommendationRule(String product_name,UUID product_id,String product_text,List<RuleQuery> rule){
    this.productName=product_name;
    this.productId=product_id;
    this.productText=product_text;
    this.rule=rule;
}

public UUID getId(){
    return id;
}

public void setId(UUID id){
    this.id=id;
}

public String getProductName(){
    return productName;
}

public void setProductName(String product_name){
    this.productName=product_name;
}

public UUID getProductId(){
    return productId;
}

public void setProductId(UUID product_id){
    this.productId=product_id;
}

public String getProductText(){
    return productText;
}

public void setProductText(String product_text){
    this.productText=product_text;
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
    return Objects.equals(id,recommendationRule.id)&&Objects.equals(productName,recommendationRule.productName)&&Objects.equals(productId,recommendationRule.productId)&&Objects.equals(productText,recommendationRule.productText)&&Objects.equals(rule,recommendationRule.rule);
}

@Override
public int hashCode(){
    return Objects.hash(id,productName,productId,productText,rule);
}

@Override
public String toString(){
    return "RecommendationRule{"+"id="+id+", product_name='"+productName+'\''+", product_id="+productId+", product_text='"+productText+'\''+", rule="+rule+'}';
}
}
