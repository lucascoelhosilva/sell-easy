package com.coelho.dto;

import com.coelho.enums.CategoryEnum;
import com.coelho.models.Category;
import com.coelho.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    private CategoryEnum type;

    private UUID customerId;

    public Category toModel() {
        return Category.builder()
                .id(getId())
                .name(getName())
                .type(getType())
                .customer(Customer.builder().id(getCustomerId()).build())
                .build();
    }
}
