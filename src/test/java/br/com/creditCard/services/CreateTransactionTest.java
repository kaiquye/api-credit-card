package br.com.creditCard.services;

import br.com.creditCard.common.CustomException;
import br.com.creditCard.entitys.ECardStatementStatus;
import br.com.creditCard.repositories.CardStatementRepository;
import br.com.creditCard.repositories.CardTransactionRepository;
import br.com.creditCard.services.transaction.create_transaction.CreateTransactionInput;
import br.com.creditCard.services.transaction.create_transaction.CreateTransactionUseCase;
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

  // compra a vista
  @Test
  void shouldCreateSingleInstallmentTransactionWithNewStatement() {
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

  // compra parcelada
  @Test
  void shouldCreateTransactionWithFourInstallments() {
    var input = CreateTransactionInput.builder()
        .accountNumber(4444444)
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
    Assertions.assertEquals(ECardStatementStatus.OPEN, statementList.getFirst().getStatus());
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

    Assertions.assertEquals("A data da compra não pode estar no passado.", exception.getMessage());
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
    Assertions.assertEquals("O valor total das parcelas não pode ser maior que o valor da compra.",
        exception.getMessage());
  }

  @Test
  void shouldFailWhenTotalPurchaseAmountIsInvalid() {
    var input = CreateTransactionInput.builder()
        .accountNumber(123455)
        .numberOfInstallments(2)
        .companyName("Inter Company")
        .installmentAmount(50.00)
        .totalPurchaseAmount(0.00)
        .purchaseDate(LocalDateTime.now())
        .build();

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.createTransactionUseCase.execute(input)
    );

    Assertions.assertEquals("O valor total da compra deve ser maior que zero.",
        exception.getMessage());
  }

  @Test
  void shouldFailWhenNumberOfInstallmentsIsInvalid() {
    var input = CreateTransactionInput.builder()
        .accountNumber(123455)
        .numberOfInstallments(0)
        .companyName("Inter Company")
        .installmentAmount(200.00)
        .totalPurchaseAmount(200.00)
        .purchaseDate(LocalDateTime.now())
        .build();

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.createTransactionUseCase.execute(input)
    );

    Assertions.assertEquals("O número de parcelas deve ser maior que zero.",
        exception.getMessage());
  }
}
