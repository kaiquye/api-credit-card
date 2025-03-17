package br.com.kaique.common;

import io.micronaut.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

  private HttpStatus status;

  public CustomException(String errorMessage, HttpStatus status) {
    super(errorMessage);
    this.status = status;
  }
}
