package br.com.kaique.services;

import br.com.kaique.common.CustomException;
import br.com.kaique.entitys.ECardStatementStatus;
import br.com.kaique.repositories.CardStatementRepository;
import br.com.kaique.repositories.CardTransactionRepository;
import br.com.kaique.services.transaction.create_transaction.CreateTransactionInput;
import br.com.kaique.services.transaction.create_transaction.CreateTransactionUseCase;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@MicronautTest
public class CreateTransactionTest {

  @Inject
  CreateTransactionUseCase createTransactionUseCase;
  @Inject
  CardTransactionRepository transactionRepository;
  @Inject
  CardStatementRepository statementRepository;

  @Test
  void shouldCreateTransactionWithFourInstallments() {
    var input = CreateTransactionInput.builder()
        .accountNumber(123455)
        .numberOfInstallments(4)
        .companyName("Inter Company")
        .installmentAmount(50.00)
        .totalPurchaseAmount(200.00)
        .purchaseDate(LocalDateTime.now())
        .build();

    var output = this.createTransactionUseCase.execute(input);
    Assertions.assertNotNull(output.getId());
    Assertions.assertEquals(output.getAccountNumber(), input.accountNumber());

    var statementList = this.statementRepository.findAllByCardId(output.getId());
    Assertions.assertEquals(statementList.size(), input.numberOfInstallments());
    Assertions.assertEquals(ECardStatementStatus.OPEN, statementList.getLast().getStatus());
    Assertions.assertEquals(statementList.getLast().getTotalAmount(), input.installmentAmount());

    var transactionListByStatement = this.transactionRepository.findAllByStatementId(
        statementList.getLast().getId());
    Assertions.assertEquals(transactionListByStatement.size(), 1);
    Assertions.assertEquals(transactionListByStatement.getFirst().getCompanyName(),
        input.companyName());
    Assertions.assertEquals(transactionListByStatement.getFirst().getTotalPurchaseAmount(),
        input.totalPurchaseAmount());
    Assertions.assertEquals(transactionListByStatement.getFirst().getInstallmentsAmount(),
        input.installmentAmount());
    Assertions.assertEquals(transactionListByStatement.getFirst().getInstallmentsCount(),
        input.numberOfInstallments());
  }

  @Test
  void shouldCreateTransactionWithOneInstallmentAndNewStatement() {
    var input = CreateTransactionInput.builder()
        .accountNumber(123455)
        .numberOfInstallments(1)
        .companyName("Inter Company")
        .installmentAmount(200.00)
        .totalPurchaseAmount(200.00)
        .purchaseDate(LocalDateTime.now())
        .build();

    var output = this.createTransactionUseCase.execute(input);
    Assertions.assertNotNull(output.getId());
    Assertions.assertEquals(output.getAccountNumber(), input.accountNumber());

    var statementList = this.statementRepository.findAllByCardId(output.getId());
    Assertions.assertEquals(statementList.size(), input.numberOfInstallments());
    Assertions.assertEquals(ECardStatementStatus.OPEN, statementList.getLast().getStatus());
    Assertions.assertEquals(statementList.getLast().getTotalAmount(), input.installmentAmount());

    var transactionListByStatement = this.transactionRepository.findAllByStatementId(
        statementList.getLast().getId());
    Assertions.assertEquals(transactionListByStatement.size(), 1);
    Assertions.assertEquals(transactionListByStatement.getFirst().getCompanyName(),
        input.companyName());
    Assertions.assertEquals(transactionListByStatement.getFirst().getTotalPurchaseAmount(),
        input.totalPurchaseAmount());
    Assertions.assertEquals(transactionListByStatement.getFirst().getInstallmentsAmount(),
        input.installmentAmount());
    Assertions.assertEquals(transactionListByStatement.getFirst().getInstallmentsCount(),
        input.numberOfInstallments());
  }

  @Test
  void shouldFailWhenPurchaseDateIsInThePast() {
    var input = CreateTransactionInput.builder()
        .accountNumber(123455)
        .numberOfInstallments(4)
        .companyName("Inter Company")
        .installmentAmount(50.00)
        .totalPurchaseAmount(200.00)
        .purchaseDate(LocalDateTime.now().minusHours(1))
        .build();

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.createTransactionUseCase.execute(input)
    );

    Assertions.assertEquals("The purchase date cannot be in the past.", exception.getMessage());
  }

  @Test
  void shouldFailWhenTransactionWithSamePurchaseDateAlreadyExists() {
    var input = CreateTransactionInput.builder()
        .accountNumber(123455)
        .numberOfInstallments(4)
        .companyName("Inter Company")
        .installmentAmount(50.00)
        .totalPurchaseAmount(200.00)
        .purchaseDate(LocalDateTime.now())
        .build();

    this.createTransactionUseCase.execute(input);

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.createTransactionUseCase.execute(input)
    );

    Assertions.assertEquals("A transaction with this purchase date already exists.",
        exception.getMessage());
  }

  @Test
  void shouldFailWhenTotalInstallmentAmountExceedsPurchaseAmount() {
    var input = CreateTransactionInput.builder()
        .accountNumber(123455)
        .numberOfInstallments(4)
        .companyName("Inter Company")
        .installmentAmount(5550.00)
        .totalPurchaseAmount(200.00)
        .purchaseDate(LocalDateTime.now())
        .build();

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.createTransactionUseCase.execute(input)
    );
    Assertions.assertEquals("The total installment amount cannot exceed the purchase amount.",
        exception.getMessage());
  }
}
