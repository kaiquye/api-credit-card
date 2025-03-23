package br.com.creditCard.controllers;

import br.com.creditCard.controllers.dtos.CreateCardTransactionRequestDto;
import br.com.creditCard.controllers.dtos.TransactionResponseDto;
import br.com.creditCard.mappers.TransactionMapper;
import br.com.creditCard.services.transaction.create_transaction.CreateTransactionInput;
import br.com.creditCard.services.transaction.create_transaction.CreateTransactionUseCase;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionInput;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionUseCase;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller("/card")
@Tag(name = "Card Transactions", description = "Operações relacionadas às transações do cartão")
public class CardTransactionController {

  @Inject
  private TransactionMapper transactionMapper;
  @Inject
  private final CreateTransactionUseCase createTransactionUseCase;
  @Inject
  private final ListTransactionUseCase listTransactionUseCase;

  @Operation(summary = "Cria uma nova transação de cartão",
      description = "Cria uma transação vinculada a um número de conta e retorna o status 201.")
  @ApiResponse(responseCode = "201", description = "Transação criada com sucesso")
  @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content)
  @Post("/transaction")
  public HttpResponse<String> Create(
      @Body @Valid @Schema(description = "Detalhes da transação a ser criada")
      CreateCardTransactionRequestDto requestDto
  ) {
    CreateTransactionInput input = new CreateTransactionInput(
        requestDto.getPurchaseDate(),
        requestDto.getCompanyName(),
        requestDto.getTotalPurchaseAmount(),
        requestDto.getInstallmentAmount(),
        requestDto.getNumberOfInstallments(),
        requestDto.getAccountNumber()
    );

    this.createTransactionUseCase.execute(input);

    return HttpResponse.status(HttpStatus.CREATED);
  }

  @Operation(summary = "Lista transações por conta e mês",
      description = "Lista todas as transações associadas a um número de conta e um mês específico.")
  @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDto.class)))
  @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
  @Get("/transaction")
  public HttpResponse<List<TransactionResponseDto>> findAllTransactionByAccountNumberAndMonth(
      @QueryValue("accountNumber") @Schema(description = "Número da conta", example = "12345") int accountNumber,
      @QueryValue("month") @Schema(description = "Mês da transação (1-12)", example = "3") int month
  ) {
    var input = new ListTransactionInput(month, accountNumber);

    var output = this.listTransactionUseCase.execute(input);

    List<TransactionResponseDto> responseBody = output.stream()
        .map(statement -> transactionMapper.toDTO(statement))
        .collect(Collectors.toList());

    return HttpResponse.status(HttpStatus.OK).body(responseBody);
  }
}
