package br.com.creditCard.services;

import br.com.creditCard.common.CustomException;
import br.com.creditCard.helper.CreateTransactionHelper;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionInput;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionUseCase;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class ListTransactionTest {

  @Inject
  ListTransactionUseCase listTransactionUseCase;
  @Inject
  CreateTransactionHelper createTransactionHelper;

  @Test
  void shouldReturnListOfTransactionByAccountNumberAndMonth() {
    var accountNumber = 1234567;
    var monthValue = LocalDateTime.now().getMonthValue();
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);

    var input = ListTransactionInput.builder()
        .accountNumber(accountNumber)
        .monthOfStatement(monthValue)
        .build();

    var output = this.listTransactionUseCase.execute(input);

    Assertions.assertEquals(output.size(), 4);
    Assertions.assertNotNull(output.getFirst().getCompanyName());
    Assertions.assertNotNull(output.getFirst().getPurchaseDate());
    Assertions.assertNotNull(output.getFirst().getTotalPurchaseAmount());
    Assertions.assertNotNull(output.getFirst().getInstallmentsAmount());
    Assertions.assertNotNull(output.getFirst().getInstallmentNumber());

    Assertions.assertEquals(output.getFirst().getCompanyName(), "New Inter Company");
    Assertions.assertEquals(output.getFirst().getInstallmentsCount(), 4);
    Assertions.assertEquals(output.getFirst().getInstallmentsAmount(), 50.0);
    Assertions.assertEquals(output.getFirst().getTotalPurchaseAmount(), 200.0);
  }

  @Test
  void shouldReturnCardNotFoundByAccountNumber() {
    var accountNumber = 1234567;
    var monthValue = LocalDateTime.now().getMonthValue();
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);

    var fakeAccountNUmber = 0;
    var input = ListTransactionInput.builder()
        .accountNumber(fakeAccountNUmber)
        .monthOfStatement(monthValue)
        .build();

    CustomException exception = Assertions.assertThrows(
        CustomException.class,
        () -> this.listTransactionUseCase.execute(input)
    );

    Assertions.assertEquals("Nenhum cartão encontrado para o número da conta informado.",
        exception.getMessage());
  }
}
