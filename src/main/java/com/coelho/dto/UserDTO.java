package com.coelho.dto;

import com.coelho.models.Customer;
import com.coelho.models.User;
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
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private UUID customerId;

    public User toModel() {
        return User.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .password(getPassword())
                .customer(Customer.builder().id(getCustomerId()).build())
                .build();
    }

}
