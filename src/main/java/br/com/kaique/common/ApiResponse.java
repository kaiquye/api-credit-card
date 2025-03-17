package br.com.kaique.common;


import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;

@Serdeable
public record ApiResponse<T>(
    T data,
    String error,
    LocalDateTime date
) {

}
