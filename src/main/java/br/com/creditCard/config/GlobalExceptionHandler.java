package br.com.creditCard.config;

import br.com.creditCard.common.ApiResponse;
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
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<ApiResponse<Void>>> {

  @Override
  public HttpResponse<ApiResponse<Void>> handle(HttpRequest request, Exception exception) {
    LocalDateTime dateError = LocalDateTime.now();
    log.error("Unhandled exception: {} | Date: {}", exception.getMessage(), dateError, exception);

    var response = new ApiResponse<Void>(null, "Internal Server Error", dateError);

    return HttpResponse.serverError(response);
  }
}
