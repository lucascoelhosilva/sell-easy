package com.coelho.config;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;

public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {

    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Money.parse(dbData);
    }
}
