package com.coelho.exceptions;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.ws.rs.core.Response;

@EqualsAndHashCode(callSuper = true)
public class Unauthorized extends BusinessException {

    public Unauthorized(@NonNull String message) {
        super(message, Response.Status.UNAUTHORIZED);
    }

}
