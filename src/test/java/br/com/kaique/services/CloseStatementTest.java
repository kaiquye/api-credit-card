package br.com.kaique.services;

import br.com.kaique.common.CustomException;
import br.com.kaique.entitys.ECardStatementStatus;
import br.com.kaique.helper.CreateTransactionHelper;
import br.com.kaique.repositories.CardStatementRepository;
import br.com.kaique.services.statement.close_statement.CloseCardStatementInput;
import br.com.kaique.services.statement.close_statement.CloseCardStatementUseCase;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class CloseStatementTest {

  @Inject
  CloseCardStatementUseCase closeCardStatementUseCase;
  @Inject
  CardStatementRepository statementRepository;
  @Inject
  CreateTransactionHelper createTransactionHelper;

  @Test
  void shouldCloseStatementSuccessfully() {
    int accountNumber = 11223344;

    var card = createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    var statementList = this.statementRepository.findAllByCardId(card.getId());

    long statementId = statementList.getFirst().getId();

    var initialStatement = statementRepository.findById(statementId);

    Assertions.assertEquals(
        ECardStatementStatus.OPEN,
        initialStatement.get().getStatus(),
        "O status inicial deveria ser OPEN antes de fechar a fatura."
    );

    var input = CloseCardStatementInput.builder()
        .accountNumber(accountNumber)
        .statementId(statementId)
        .build();

    closeCardStatementUseCase.execute(input);

    var finalStatement = statementRepository.findById(statementId);

    Assertions.assertEquals(
        ECardStatementStatus.FINALIZED,
        finalStatement.get().getStatus(),
        "O status final deveria ser FINALIZED após o fechar a fatura."
    );
  }

  @Test
  void shouldThrowExceptionWhenStatementStatusDoesNotAllowClosing() {
    int accountNumber = 23321212;

    var card = createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    var statementList = this.statementRepository.findAllByCardId(card.getId());

    long statementId = statementList.getFirst().getId();

    var input = CloseCardStatementInput.builder()
        .accountNumber(accountNumber)
        .statementId(statementId)
        .build();

    this.closeCardStatementUseCase.execute(input);

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.closeCardStatementUseCase.execute(input)
    );

    Assertions.assertEquals("Statement status does not allow this action.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenStatementNotFound() {
    int accountNumber = 1234567;

    createTransactionHelper.execute(accountNumber);

    long invalidStatementId = 0L;

    var input = CloseCardStatementInput.builder()
        .accountNumber(accountNumber)
        .statementId(invalidStatementId)
        .build();

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.closeCardStatementUseCase.execute(input)
    );

    Assertions.assertEquals("Statement not found.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenCardNotFound() {
    int invalidAccountNumber = 0;

    var input = CloseCardStatementInput.builder()
        .accountNumber(invalidAccountNumber)
        .statementId(1L)
        .build();

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.closeCardStatementUseCase.execute(input)
    );

    Assertions.assertEquals("Card not found.", exception.getMessage());
  }
}
