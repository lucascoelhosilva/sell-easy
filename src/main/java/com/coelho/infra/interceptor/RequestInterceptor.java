package com.coelho.infra.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.concurrent.TimeUnit;

@Slf4j
@Provider
public class RequestInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

    private final ThreadLocal<StopWatch> stopWatch = ThreadLocal.withInitial(StopWatch::new);

    @ConfigProperty(name = "quarkus.application.request-interceptor.threshold")
    long threshold;

    @ConfigProperty(name = "quarkus.application.request-interceptor.enabled")
    boolean enabled;

    @Override
    public void filter(ContainerRequestContext request) {
        stopWatch.remove();
        stopWatch.get().start();

        if (enabled) {
            log.info("Received request: {} {}", request.getMethod(), request.getUriInfo().getRequestUri());
        }
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {

        StopWatch stopWatchResponse = this.stopWatch.get();

        long endTime = stopWatchResponse.getTime(TimeUnit.MICROSECONDS);
        if (enabled) {
            if (endTime > threshold) {
                log.warn("Finished request {} {}. Time spent is very high: {}ms", request.getMethod(),
                        request.getUriInfo().getRequestUri(), endTime);
            } else {
                log.info("Finished request {} {}. Time spent: {}ms", request.getMethod(), request.getUriInfo().getRequestUri(), endTime);
            }
        }

        stopWatch.set(new StopWatch());
    }

}
