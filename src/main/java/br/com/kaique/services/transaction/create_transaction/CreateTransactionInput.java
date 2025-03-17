package br.com.kaique.services.transaction.create_transaction;

import java.time.LocalDateTime;

public record CreateTransactionInput(
    LocalDateTime purchaseDate,
    String companyName,
    Double totalPurchaseAmount,
    Double installmentAmount,
    int numberOfInstallments,
    int accountNumber
) {

}
