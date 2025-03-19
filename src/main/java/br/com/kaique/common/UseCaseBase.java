package br.com.kaique.common;

public interface UseCaseBase<Input, Output> {

  Output execute(Input data);
}
