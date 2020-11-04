package com.coelho.infra.error;

import com.coelho.exceptions.BusinessException;
import io.jaegertracing.internal.JaegerSpanContext;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.vertx.core.http.HttpServerRequest;
import lombok.NonNull;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

@Provider
public class ExceptionHandler implements ExceptionMapper<BusinessException> {

    @Inject
    Tracer tracer;

    @Inject
    MeterRegistry meterRegistry;

    @Context
    HttpServerRequest request;

    @Override
    public Response toResponse(BusinessException exception) {
        APIErrorMessage message = APIErrorMessage.builder()
                .code(exception.getHttpStatus().toString())
                .description(exception.getMessage())
                .build();

        return Response
                .status(exception.getHttpStatus())
                .entity(buildAPIErrorAndRegisterMetrics(message))
                .build();
    }

    private APIError buildAPIError(@NonNull APIErrorMessage messages) {
        return APIError.builder()
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .traceId(getTraceId())
                .message(messages)
                .build();
    }

    private void registerMetrics(@NonNull APIError apiError) {
        // Set Jaeger Trace Error
        tracer.activeSpan().setTag(String.valueOf(Tags.ERROR), Boolean.TRUE);

        // Prometheus Metrics
        Collection<Tag> tags = new ArrayList<>();
        tags.addAll(apiError.toTags());
        tags.add(Tag.of("method", request.method().name()));
        tags.add(Tag.of("uri", request.absoluteURI()));

        meterRegistry.counter("http_server_request_error", tags).increment();
    }

    private APIError buildAPIErrorAndRegisterMetrics(@NonNull APIErrorMessage messages) {
        APIError apiError = buildAPIError(messages);
        registerMetrics(apiError);

        return apiError;
    }

    private String getTraceId() {
        Span s = tracer.activeSpan();
        SpanContext ctx = s == null ? null : s.context();
        if (ctx != null && ctx instanceof JaegerSpanContext) {
            //TODO: Fix this downcasting once Quarkus upgrades to Jaeger 0.35+
            return ((JaegerSpanContext) ctx).getTraceId();
        } else {
            return null;
        }
    }
}
