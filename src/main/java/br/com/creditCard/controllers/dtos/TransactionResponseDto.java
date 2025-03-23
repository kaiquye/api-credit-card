package br.com.creditCard.controllers.dtos;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder()
@Introspected
@Setter
@Getter
@AllArgsConstructor
@Serdeable
@Schema(description = "Representação de uma transação de cartão")
public class TransactionResponseDto {

  @Schema(description = "Nome do estabelecimento", example = "Supermercado ABC")
  private String companyName;

  @Schema(description = "Número total de parcelas", example = "12")
  private int installmentsCount;

  @Schema(description = "Número da parcela atual", example = "3")
  private int installmentNumber;

  @Schema(description = "Valor de cada parcela", example = "100.50")
  private double installmentsAmount;

  @Schema(description = "Valor total da compra", example = "1200.00")
  private double totalPurchaseAmount;

  @Schema(description = "Data da compra", example = "2024-03-15T14:30:00")
  private LocalDateTime purchaseDate;
}
