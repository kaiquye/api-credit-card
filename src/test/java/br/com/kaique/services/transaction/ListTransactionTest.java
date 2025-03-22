package br.com.kaique.services.transaction;

import br.com.kaique.common.CustomException;
import br.com.kaique.helper.CreateTransactionHelper;
import br.com.kaique.services.transaction.list_transaction.ListTransactionInput;
import br.com.kaique.services.transaction.list_transaction.ListTransactionUseCase;
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
    Assertions.assertNotNull(output.getFirst().getId());
    Assertions.assertNotNull(output.getFirst().getCompanyName());
    Assertions.assertNotNull(output.getFirst().getPurchaseDate());
    Assertions.assertNotNull(output.getFirst().getTotalPurchaseAmount());
    Assertions.assertNotNull(output.getFirst().getInstallmentsAmount());
    Assertions.assertNotNull(output.getFirst().getCurrentInstallmentsNumber());

    Assertions.assertEquals(output.getFirst().getId(), 1);
    Assertions.assertEquals(output.getFirst().getCompanyName(), "New Inter Company");
    Assertions.assertEquals(output.getFirst().getInstallmentsCount(), 4);
    Assertions.assertEquals(output.getFirst().getInstallmentsAmount(), 50.0);
    Assertions.assertEquals(output.getFirst().getCurrentInstallmentsNumber(), 1);
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

    Assertions.assertEquals("Card by account number not found",
        exception.getMessage());
  }
}
