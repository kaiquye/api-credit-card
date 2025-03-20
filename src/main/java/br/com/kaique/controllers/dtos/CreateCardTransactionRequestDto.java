package br.com.kaique.controllers.dtos;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Introspected
@Setter
@Getter
@RequiredArgsConstructor
@Serdeable
@Schema(description = "Requisição para criar uma transação de cartão")
public class CreateCardTransactionRequestDto {

  @NotNull(message = "Purchase date cannot be null")
  @Schema(description = "Data da compra", example = "2024-03-15T14:30:00")
  private LocalDateTime purchaseDate;

  @NotBlank(message = "Company name is required")
  @Schema(description = "Nome do estabelecimento", example = "Supermercado ABC")
  private String companyName;

  @NotNull(message = "Total purchase amount is required")
  @Positive(message = "Total purchase amount must be positive")
  @Schema(description = "Valor total da compra", example = "1200.00")
  private Double totalPurchaseAmount;

  @NotNull(message = "Installment amount is required")
  @PositiveOrZero(message = "Installment amount cannot be negative")
  @Schema(description = "Valor de cada parcela", example = "100.50")
  private Double installmentAmount;

  @Min(value = 1, message = "Number of installments must be at least 1")
  @Schema(description = "Número total de parcelas", example = "12")
  private int numberOfInstallments;

  @Min(value = 1, message = "Account number must be positive")
  @Schema(description = "Número da conta", example = "987654")
  private int accountNumber;
}
