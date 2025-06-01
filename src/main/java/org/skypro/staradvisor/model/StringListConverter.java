package org.skypro.staradvisor.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ";";

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return list != null ? String.join(DELIMITER, list) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String str) {
        return str != null && !str.isBlank()
                ? Arrays.asList(str.split(DELIMITER))
                : new ArrayList<>();
    }
}
