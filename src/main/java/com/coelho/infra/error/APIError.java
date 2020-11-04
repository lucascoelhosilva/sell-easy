package com.coelho.infra.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class APIError {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);

    private String traceId;

    private APIErrorMessage message;

    @JsonIgnore
    public Collection<Tag> toTags() {
        return Optional.ofNullable(message)
                .stream()
                .map(value -> Tag.of("error", value.getCode()))
                .collect(Collectors.toList());
    }

}
