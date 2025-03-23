package br.com.creditCard.services.transaction.create_transaction.impl;

import br.com.creditCard.common.CustomException;
import br.com.creditCard.entitys.*;
import br.com.creditCard.repositories.*;
import br.com.creditCard.services.transaction.create_transaction.*;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;

@Singleton
@RequiredArgsConstructor
public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

  private final CardTransactionInstallmentRepository installmentRepository;
  private final CardTransactionRepository transactionRepository;
  private final CardStatementRepository statementRepository;
  private final CardRepository cardRepository;

  @Transactional
  @Override
  public Card execute(CreateTransactionInput input) {
    if (input.numberOfInstallments() <= 0) {
      throw new CustomException("O número de parcelas deve ser maior que zero.",
          HttpStatus.BAD_REQUEST);
    }

    if (input.totalPurchaseAmount() <= 0) {
      throw new CustomException("O valor total da compra deve ser maior que zero.",
          HttpStatus.BAD_REQUEST);
    }

    // Buscando cartão pelo número da conta (o cartão representa a conta do usuário)
    Card card = findOrCreateCard(input.accountNumber());

    validatePurchaseDate(input.purchaseDate());
    validateInstallmentAmounts(input);

    if (transactionRepository.findByPurchaseDate(input.purchaseDate()).isPresent()) {
      throw new CustomException("Já existe uma transação registrada com essa data de compra.",
          HttpStatus.CONFLICT);
    }

    CardStatement activeStatement = statementRepository.findStatementOpenedByCardId(card.getId())
        .orElseThrow(() -> new CustomException("Nenhuma fatura ativa encontrada para este cartão.",
            HttpStatus.CONFLICT));

    CardTransaction transaction = createTransaction(input, activeStatement);
    transactionRepository.save(transaction);

    List<CardTransactionInstallment> installmentList = createInstallments(input, transaction, card);

    List<CardStatement> statementList = processStatements(card, installmentList, transaction);
    statementRepository.saveAll(statementList);

    transaction.getTransactionInstallmentList().addAll(installmentList);
    transactionRepository.save(transaction);

    linkInstallmentsToStatements(installmentList, statementList);
    installmentRepository.saveAll(installmentList);

    return card;
  }

  private Card findOrCreateCard(int accountNumber) {
    var cardOptional = cardRepository.findByAccountNumber(accountNumber);
    if (cardOptional.isPresent()) {
      return cardOptional.get();
    }

    Card newCard = new Card();
    newCard.setAccountNumber(accountNumber);
    newCard = cardRepository.save(newCard);

    CardStatement newStatement = new CardStatement();
    newStatement.setCard(newCard);
    newStatement.setTotalAmount(0.0);
    newStatement.setStartedAt(LocalDateTime.now());
    newStatement.setDueDate(LocalDateTime.now().plusMonths(1));
    newStatement.setStatus(ECardStatementStatus.OPEN);
    statementRepository.save(newStatement);

    return newCard;
  }

  private void validatePurchaseDate(LocalDateTime purchaseDate) {
    if (purchaseDate.isBefore(LocalDateTime.now().minusMinutes(3))) {
      throw new CustomException("A data da compra não pode estar no passado.",
          HttpStatus.BAD_REQUEST);
    }
  }

  private void validateInstallmentAmounts(CreateTransactionInput input) {
    double totalCalculated = input.numberOfInstallments() * input.installmentAmount();
    if (input.installmentAmount() > input.totalPurchaseAmount()
        || totalCalculated > input.totalPurchaseAmount()) {
      throw new CustomException(
          "O valor total das parcelas não pode ser maior que o valor da compra.",
          HttpStatus.BAD_REQUEST);
    }
  }

  private void linkInstallmentsToStatements(List<CardTransactionInstallment> installments,
      List<CardStatement> statements) {
    for (int i = 0; i < installments.size(); i++) {
      installments.get(i).setStatement(statements.get(i));
    }
  }

  private CardTransaction createTransaction(CreateTransactionInput input, CardStatement statement) {
    CardTransaction transaction = new CardTransaction();
    transaction.setCompanyName(input.companyName());
    transaction.setInstallmentsCount(input.numberOfInstallments());
    transaction.setInstallmentsAmount(input.installmentAmount());
    transaction.setTotalPurchaseAmount(input.totalPurchaseAmount());
    transaction.setPurchaseDate(input.purchaseDate());
    transaction.getStatements().add(statement);

    return transaction;
  }

  private List<CardTransactionInstallment> createInstallments(CreateTransactionInput input,
      CardTransaction transaction, Card card) {
    List<CardTransactionInstallment> installments = new ArrayList<>();

    // Pegando o número da última parcela registrada
    var lastInstallment = this.installmentRepository.findLastByCardIdAndTransactionId(card.getId(),
        transaction.getId());
    int lastInstallmentNumber = lastInstallment.isPresent()
        ? lastInstallment.get().getInstallmentNumber()
        : 1;

    for (int i = 0; i < input.numberOfInstallments(); i++) {
      CardTransactionInstallment installment = new CardTransactionInstallment();
      installment.setInstallmentNumber(lastInstallmentNumber + i);
      installment.setAmount(input.installmentAmount());
      installment.setTransaction(transaction);
      installments.add(installment);
    }

    return installments;
  }

  private List<CardStatement> processStatements(Card card,
      List<CardTransactionInstallment> installments,
      CardTransaction transaction) {
    List<CardStatement> statements = statementRepository.findByStatusInAndCardIdOrderByStartedAt(
        List.of(ECardStatementStatus.OPEN, ECardStatementStatus.FUTURE), card.getId());

    // Atualizando faturas existentes
    for (int i = 0; i < statements.size() && i < installments.size(); i++) {
      CardStatement statement = statements.get(i);
      CardTransactionInstallment installment = installments.get(i);
      statement.setTotalAmount(statement.getTotalAmount() + installment.getAmount());
      statement.getTransactions().add(transaction);
      statement.getTransactionInstallmentList().add(installment);
    }

    // Criando novas faturas (se necessário)
    for (int i = statements.size(); i < installments.size(); i++) {
      LocalDateTime startDate = statements.getLast().getStartedAt().plusMonths(1);
      LocalDateTime dueDate = startDate.plusMonths(1);

      CardStatement futureStatement = new CardStatement();
      futureStatement.setCard(card);
      futureStatement.setTotalAmount(installments.get(i).getAmount());
      futureStatement.getTransactions().add(transaction);
      futureStatement.setStartedAt(startDate);
      futureStatement.setDueDate(dueDate);
      futureStatement.setStatus(ECardStatementStatus.FUTURE);
      futureStatement.getTransactionInstallmentList().add(installments.get(i));

      statements.add(futureStatement);
    }

    return statements;
  }
}
