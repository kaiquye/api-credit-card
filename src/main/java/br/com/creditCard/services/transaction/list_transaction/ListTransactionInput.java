package br.com.creditCard.services.transaction.list_transaction;

import lombok.Builder;

@Builder
public record ListTransactionInput(
    int monthOfStatement,
    int accountNumber
) {

}
