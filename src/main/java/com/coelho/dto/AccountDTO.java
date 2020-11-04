package com.coelho.dto;

import com.coelho.models.Account;
import com.coelho.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    private MonetaryAmount total;

    private UUID customerId;

    public Account toModel() {
        return Account.builder()
                .id(getId())
                .name(getName())
                .customer(Customer.builder().id(getCustomerId()).build())
                .total(getTotal())
                .build();
    }


}
