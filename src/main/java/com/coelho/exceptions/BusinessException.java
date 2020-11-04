package com.coelho.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.ws.rs.core.Response;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class BusinessException extends RuntimeException {

    private final String message;

    private Response.Status httpStatus;

    public BusinessException(@NonNull String message) {
        super(message);
        this.message = message;
        this.httpStatus = Response.Status.BAD_REQUEST;
    }

    public BusinessException(@NonNull String message, Response.Status httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
