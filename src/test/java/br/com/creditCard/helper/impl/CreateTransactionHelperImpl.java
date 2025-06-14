package br.com.creditCard.helper.impl;

import br.com.creditCard.entitys.Card;
import br.com.creditCard.helper.CreateTransactionHelper;
import br.com.creditCard.services.transaction.create_transaction.CreateTransactionInput;
import br.com.creditCard.services.transaction.create_transaction.CreateTransactionUseCase;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.time.LocalDateTime;

@Singleton
public class CreateTransactionHelperImpl implements CreateTransactionHelper {

  @Inject
  private final CreateTransactionUseCase createTransactionUseCase;

  public CreateTransactionHelperImpl(CreateTransactionUseCase createTransactionUseCase) {
    this.createTransactionUseCase = createTransactionUseCase;
  }

  public Card execute(int accountNumber) {
    var input = CreateTransactionInput.builder()
        .accountNumber(accountNumber)
        .numberOfInstallments(4)
        .companyName("New Inter Company")
        .installmentAmount(50.00)
        .totalPurchaseAmount(200.00)
        .purchaseDate(LocalDateTime.now())
        .build();

    var output = this.createTransactionUseCase.execute(input);

    return output;
  }
}
