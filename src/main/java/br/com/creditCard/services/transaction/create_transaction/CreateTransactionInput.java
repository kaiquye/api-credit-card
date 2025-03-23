package br.com.creditCard.services.transaction.create_transaction;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder()
public record CreateTransactionInput(
    LocalDateTime purchaseDate,
    String companyName,
    Double totalPurchaseAmount,
    Double installmentAmount,
    int numberOfInstallments,
    int accountNumber
) {

}
