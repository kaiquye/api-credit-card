package br.com.creditCard.services.statement.close_statement.impl;

import br.com.creditCard.common.CustomException;
import br.com.creditCard.entitys.Card;
import br.com.creditCard.entitys.CardStatement;
import br.com.creditCard.entitys.ECardStatementStatus;
import br.com.creditCard.repositories.CardRepository;
import br.com.creditCard.repositories.CardStatementRepository;
import br.com.creditCard.services.statement.close_statement.CloseCardStatementInput;
import br.com.creditCard.services.statement.close_statement.CloseCardStatementUseCase;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Singleton
public class CloseStatementUseCaseImpl implements CloseCardStatementUseCase {

  @Inject
  private final CardStatementRepository cardStatementRepository;
  @Inject
  private final CardRepository cardRepository;

  // Fazendo o fechamento da fatura.
  @Transactional
  @Override
  public Void execute(CloseCardStatementInput data) {
    var card = this.findCardByAccountNumberOfFail(data.accountNumber());

    // buscando a fatura
    var statement = this.findStatementByCardOrFail(card, data.statementId());
    if (!statement.getStatus().equals(ECardStatementStatus.OPEN)) {
      throw new CustomException("A fatura não pode ser fechada porque não está com o status de aberta.",
          HttpStatus.NOT_FOUND);
    }
    statement.setDueDate(LocalDateTime.now()); // atualizando a data de fechamento
    statement.setStatus(ECardStatementStatus.FINALIZED); // alterando o status.

    this.cardStatementRepository.update(statement);

    // Atualizando o status da fatura futura para OPEN ou criando uma nova
    var nextStatementOptional = this.findNextStatement(card);
    if (nextStatementOptional.isPresent()) {
      var nextStatement = nextStatementOptional.get();
      nextStatement.setStatus(ECardStatementStatus.OPEN);

      this.cardStatementRepository.update(nextStatement);
      return null;
    }

    var newStatement = new CardStatement();
    var nextMonth = LocalDate.now().plusMonths(1).atStartOfDay();

    newStatement.setCard(card);
    newStatement.setTotalAmount(0.0);
    newStatement.setStartedAt(nextMonth);
    newStatement.setDueDate(LocalDateTime.now().plusMonths(1));
    newStatement.setStatus(ECardStatementStatus.OPEN);

    this.cardStatementRepository.save(newStatement);
    return null;
  }

  private Card findCardByAccountNumberOfFail(int accountNumber) {
    return this.cardRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new CustomException("Card not found.", HttpStatus.NOT_FOUND));
  }

  private CardStatement findStatementByCardOrFail(Card card,
      Long cardStatementId) {
    return this.cardStatementRepository.findStatementOpenedByIdAndCardId(cardStatementId,
            card.getId())
        .orElseThrow(() -> new CustomException("Statement not found.", HttpStatus.NOT_FOUND));
  }

  private Optional<CardStatement> findNextStatement(Card card) {
    var nextMonth = LocalDate.now().plusMonths(1);
    var startOfMonth = nextMonth.withDayOfMonth(1).atStartOfDay();
    var endOfMonth = nextMonth.withDayOfMonth(nextMonth.lengthOfMonth()).atTime(23, 59, 59);

    return this.cardStatementRepository.findStatementByStatusAndMonthAndCardId(
        startOfMonth, endOfMonth,
        ECardStatementStatus.FUTURE, card.getId());
  }
}
