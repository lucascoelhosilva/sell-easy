package com.coelho.events;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.money.MonetaryAmount;
import java.util.UUID;

@Builder
@Data
@Slf4j
public class TransactionEvent {

    private UUID accountId;
    private TransactionType type;
    private MonetaryAmount value;

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public MonetaryAmount getValue() {
        return value;
    }

    public void setValue(MonetaryAmount value) {
        this.value = value;
    }
}
