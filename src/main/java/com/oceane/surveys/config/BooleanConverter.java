package com.oceane.surveys.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean javaAttribute) {
        if (javaAttribute == null) {
            return null;
        }

        return javaAttribute ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbValue) {
        if (dbValue == null) {
            return null;
        }

        return "Y".equals(dbValue);
    }
}
