package br.com.kaique.config;

import br.com.kaique.common.ApiResponse;
import br.com.kaique.common.CustomException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces
@Singleton
@Requires(classes = {CustomException.class, ExceptionHandler.class})
public class CustomExceptionHandler implements
    ExceptionHandler<CustomException, HttpResponse<ApiResponse<Void>>> {

  @Override
  public HttpResponse<ApiResponse<Void>> handle(HttpRequest request, CustomException exception) {
    var dateError = LocalDateTime.now();
    log.info("Error: ", exception.getMessage(), "Date: ", dateError);

    var response = new ApiResponse<Void>(null,
        exception.getMessage(),
        dateError
    );
    
    return HttpResponse.status(exception.getStatus()).body(response);
  }
}
