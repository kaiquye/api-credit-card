package br.com.creditCard.helper;

import br.com.creditCard.entitys.Card;

public interface CreateTransactionHelper {

  Card execute(int accountNumber);
}
