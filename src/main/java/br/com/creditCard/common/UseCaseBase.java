package br.com.creditCard.common;

public interface UseCaseBase<Input, Output> {

  Output execute(Input data);
}
