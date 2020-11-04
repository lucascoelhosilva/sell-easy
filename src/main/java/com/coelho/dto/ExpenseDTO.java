package com.coelho.dto;

import com.coelho.models.Account;
import com.coelho.models.Category;
import com.coelho.models.Customer;
import com.coelho.models.Expense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "AccountId is required")
    private UUID accountId;

    @NotNull(message = "CategoryId is required")
    private UUID categoryId;

    @NotNull(message = "CustomerId is required")
    private UUID customerId;

    private LocalDate date;

    private Boolean paid;

    private Boolean reminder;

    private Boolean recurrent;

    private MonetaryAmount value;

    public Expense toModel() {
        return Expense.builder()
                .id(getId())
                .description(getDescription())
                .account(Account.builder().id(getAccountId()).build())
                .category(Category.builder().id(getCategoryId()).build())
                .customer(Customer.builder().id(getCustomerId()).build())
                .date(getDate())
                .paid(getPaid())
                .reminder(getReminder())
                .recurrent(getRecurrent())
                .value(getValue())
                .build();
    }

}
