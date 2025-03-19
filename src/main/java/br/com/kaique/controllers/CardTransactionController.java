package br.com.kaique.controllers;

import br.com.kaique.controllers.dtos.card_transaction.CreateCardTransactionRequestDto;
import br.com.kaique.entitys.Card;
import br.com.kaique.repositories.CardRepository;
import br.com.kaique.services.transaction.create_transaction.CreateTransactionInput;
import br.com.kaique.services.transaction.create_transaction.CreateTransactionUseCase;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller("/card/transaction")
public class CardTransactionController {

  @Inject()
  private final CreateTransactionUseCase createTransactionUseCase;

  @Inject()
  private final CardRepository cardRepository;

  @Post()
  public HttpResponse<String> Create(@Body @Valid CreateCardTransactionRequestDto requestDto) {

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

  @Get()
  public HttpResponse<List<Card>> test() {
    var card = this.cardRepository.findAll();
    return HttpResponse.status(HttpStatus.OK).body(card);
  }
}
