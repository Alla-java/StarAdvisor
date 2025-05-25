package org.skypro.staradvisor.model;

import java.util.Objects;
import java.util.UUID;

public class RecommendationDto {

private String name;
private UUID id;
private String text;

public RecommendationDto(UUID id, String name, String text){
    this.name=name;
    this.id=id;
    this.text=text;
}

public String getName(){
    return name;
}

public void setName(String name){
    this.name=name;
}

public UUID getId(){
    return id;
}

public void setId(UUID id){
    this.id=id;
}

public String getText(){
    return text;
}

public void setText(String text){
    this.text=text;
}

@Override
public boolean equals(Object o){
    if(this==o)
        return true;
    if(o==null||getClass()!=o.getClass())
        return false;
    RecommendationDto that=(RecommendationDto)o;
    return Objects.equals(name,that.name)&&Objects.equals(id,that.id)&&Objects.equals(text,that.text);
}

@Override
public int hashCode(){
    return Objects.hash(name,id,text);
}

@Override
public String toString(){
    return "RecommendationDTO{"+"name='"+name+'\''+", id="+id+", text='"+text+'\''+'}';
}
}
