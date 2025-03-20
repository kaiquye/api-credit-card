package br.com.kaique.services.transaction.list_transaction;

public record ListTransactionInput(
    int monthOfStatement,
    int accountNumber
) {

}
