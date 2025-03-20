package br.com.kaique.controllers;

import br.com.kaique.services.statement.close_statement.CloseCardStatementInput;
import br.com.kaique.services.statement.close_statement.CloseCardStatementUseCase;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller("/card")
@Tag(name = "Card Statements", description = "Operações relacionadas aos extratos do cartão")
public class CardStatementController {

  @Inject
  private final CloseCardStatementUseCase closeCardStatementUseCase;

  @Patch("/statement/{statementId}/account/{accountNumber}")
  @Operation(
      summary = "Fecha uma fatura do cartão",
      description = "Fecha uma fatura do cartão com base no ID do fatura e no número da conta."
  )
  @ApiResponse(responseCode = "200", description = "Extrato fechado com sucesso")
  @ApiResponse(responseCode = "400", description = "Parâmetros inválidos na requisição")
  @ApiResponse(responseCode = "404", description = "Extrato ou conta não encontrados")
  public void closeStatement(
      @PathVariable @Parameter(description = "ID do fatura a ser fechado") Long statementId,
      @PathVariable @Parameter(description = "Número da conta associada ao fatura") int accountNumber
  ) {
    var input = new CloseCardStatementInput(accountNumber, statementId);
    this.closeCardStatementUseCase.execute(input);
  }
}
