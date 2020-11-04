package com.coelho.exceptions;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.ws.rs.core.Response;

@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends BusinessException {

    public NotFoundException(@NonNull String message) {
        super(message, Response.Status.NOT_FOUND);
    }

}
