package com.miri.orderservice.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Convert
public class StringEncryptUniqueConverter implements AttributeConverter<String, String> {
    private final AESUtils aesUtils;

    public StringEncryptUniqueConverter(AESUtils aesUtils) {
        this.aesUtils = aesUtils;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (StringUtils.isBlank(attribute)) return attribute;

        return aesUtils.encodeUnique(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) return dbData;

        return aesUtils.decodeUnique(dbData);
    }
}