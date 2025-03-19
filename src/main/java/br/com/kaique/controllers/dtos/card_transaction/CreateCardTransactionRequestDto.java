package br.com.kaique.controllers.dtos.card_transaction;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Introspected()
@Setter()
@Getter()
@RequiredArgsConstructor()
@Serdeable
public class CreateCardTransactionRequestDto {

  @NotNull(message = "Purchase date cannot be null")
  private LocalDateTime purchaseDate;

  @NotBlank(message = "Company name is required")
  private String companyName;

  @NotNull(message = "Total purchase amount is required")
  @Positive(message = "Total purchase amount must be positive")
  private Double totalPurchaseAmount;

  @NotNull(message = "Installment amount is required")
  @PositiveOrZero(message = "Installment amount cannot be negative")
  private Double installmentAmount;

  @Min(value = 1, message = "Number of installments must be at least 1")
  private int numberOfInstallments;

  @Min(value = 1, message = "Account number must be positive")
  private int accountNumber;

}
