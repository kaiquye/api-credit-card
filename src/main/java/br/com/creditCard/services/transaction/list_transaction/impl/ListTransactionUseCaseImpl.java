package br.com.creditCard.services.transaction.list_transaction.impl;

import br.com.creditCard.common.CustomException;
import br.com.creditCard.repositories.CardRepository;
import br.com.creditCard.repositories.CardStatementRepository;
import br.com.creditCard.repositories.CardTransactionInstallmentRepository;
import br.com.creditCard.repositories.CardTransactionRepository;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionInput;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionOutput;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionUseCase;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Singleton
public class ListTransactionUseCaseImpl implements ListTransactionUseCase {

  private final CardRepository cardRepository;
  private final CardTransactionRepository cardTransactionRepository;
  private final CardStatementRepository cardStatementRepository;
  private final CardTransactionInstallmentRepository cardTransactionInstallmentRepository;

  @Override
  public List<ListTransactionOutput> execute(ListTransactionInput data) {
    var cardOptional = this.cardRepository.findByAccountNumber(data.accountNumber());
    if (cardOptional.isEmpty()) {
      throw new CustomException("Card by account number not found", HttpStatus.NOT_FOUND);
    }
    var card = cardOptional.get();

    int month = data.monthOfStatement();
    int year = LocalDate.now().getYear();

    var cardStatementOptional = this.cardStatementRepository.findByCardIdAndMonth(
        card.getId(), month, year);
    if (cardStatementOptional.isEmpty()) {
      throw new CustomException("Statement by account number not found", HttpStatus.NOT_FOUND);
    }

    var listOfTransactionByStatement = this.cardTransactionRepository.findAllByStatementId(
        cardStatementOptional.get().getId());

    return listOfTransactionByStatement.stream().map((transaction -> {
      var currentInstallment = this.cardTransactionInstallmentRepository.findByTransactionIdAndCardId(
          transaction.getId(), card.getId());
      return new ListTransactionOutput(transaction,
          currentInstallment.get().getInstallmentNumber());
    })).toList();
  }
}
