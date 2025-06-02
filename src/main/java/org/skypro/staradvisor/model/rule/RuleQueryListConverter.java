package org.skypro.staradvisor.model.rule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter(autoApply=false)
public class RuleQueryListConverter implements AttributeConverter<List<RuleQuery>,String>{

private final ObjectMapper objectMapper=new ObjectMapper();

@Override
public String convertToDatabaseColumn(List<RuleQuery> attribute){
    if(attribute==null)
        return "[]";
    try{
        return objectMapper.writeValueAsString(attribute);
    }catch(JsonProcessingException e){
        throw new IllegalArgumentException("Ошибка сериализации правила",e);
    }
}

@Override
public List<RuleQuery> convertToEntityAttribute(String dbData){
    if(dbData==null||dbData.isBlank())
        return List.of();
    try{
        return objectMapper.readValue(dbData,new TypeReference<>(){
        });
    }catch(IOException e){
        throw new IllegalArgumentException("Ошибка десериализации правила",e);
    }
}
}