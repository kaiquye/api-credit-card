package br.com.kaique.services.transaction.create_transaction.impl;

import br.com.kaique.common.CustomException;
import br.com.kaique.entitys.Card;
import br.com.kaique.entitys.CardStatement;
import br.com.kaique.entitys.CardTransaction;
import br.com.kaique.entitys.CardTransactionInstallment;
import br.com.kaique.entitys.ECardStatementStatus;
import br.com.kaique.repositories.CardRepository;
import br.com.kaique.repositories.CardStatementRepository;
import br.com.kaique.repositories.CardTransactionInstallmentRepository;
import br.com.kaique.repositories.CardTransactionRepository;
import br.com.kaique.services.transaction.create_transaction.CreateTransactionInput;
import br.com.kaique.services.transaction.create_transaction.CreateTransactionOutput;
import br.com.kaique.services.transaction.create_transaction.CreateTransactionUseCase;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor()
public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

  @Inject()
  private final CardTransactionInstallmentRepository cardTransactionInstallmentRepository;

  @Inject()
  private final CardTransactionRepository cardTransactionRepository;

  @Inject()
  private final CardStatementRepository cardStatementRepository;

  @Inject()
  private final CardRepository cardRepository;

  @Transactional()
  @Override()
  public CreateTransactionOutput execute(CreateTransactionInput data) {
    Card card = this.findOrCreateCardByAccountNumber(data.accountNumber());

    // VERIFICAR SE A DATA DA COMPRA N'AO E PASSADA (COLOCAR O TIME DE 1 MINUTOS)
    this.validateIfPurchaseDateIsValid(data.purchaseDate());

    // VERIFICAR SE O VALOR DAS PARCELAS N'AO FICA MAIOR QUE O VALOR DO PRODUTO
    // VERIFICAR SE O VALOR DA PARCELA E MAIOR QUE O VALOR DO PRODUTO
    this.validateTotalInstallmentAmount(data.numberOfInstallments(), data.installmentAmount(),
        data.totalPurchaseAmount());

    // VERIFICAR SE A FATURA NAO E DUPLICADA PELA A DATA
    Optional<CardTransaction> existingTransaction = this.cardTransactionRepository.findByPurchaseDate(
        data.purchaseDate());
    if (existingTransaction.isPresent()) {
      throw new CustomException("A transaction with this purchase date already exists.",
          HttpStatus.CONFLICT);
    }

    // VERIFICAR SE EXISTE FATURA EM ABERTO
    Optional<CardStatement> activeCardStatementOptional = this.cardStatementRepository.findStatementOpenedByCardId(
        card.getId());
    if (activeCardStatementOptional.isEmpty()) {
      throw new CustomException("No active statement found.", HttpStatus.CONFLICT);
    }
    CardStatement activeCardStatement = activeCardStatementOptional.get();

    // CRIAR NOVAS FATURAS PELA QUANTIDADE DE PARCELAS

    CardTransaction cardTransaction = new CardTransaction();
    cardTransaction.setCompanyName(data.companyName());
    cardTransaction.setInstallmentsCount(data.numberOfInstallments());
    cardTransaction.setInstallmentsAmount(data.installmentAmount());
    cardTransaction.setTotalPurchaseAmount(data.totalPurchaseAmount());
    cardTransaction.setPurchaseDate(data.purchaseDate());
    cardTransaction.setStatement(activeCardStatement);

    List<CardTransactionInstallment> cardTransactionInstallmentList = new ArrayList<>();
    for (var i = 0; data.numberOfInstallments() >= i; i++) {
      CardTransactionInstallment cardTransactionInstallment = new CardTransactionInstallment();

      cardTransactionInstallment.setAmount(data.installmentAmount());
      cardTransactionInstallment.setInstallmentNumber(data.numberOfInstallments());
      cardTransactionInstallment.setStatement(activeCardStatement);
      cardTransactionInstallment.setAmount(data.installmentAmount());
      cardTransactionInstallment.setTransaction(cardTransaction);

      cardTransactionInstallmentList.add(cardTransactionInstallment);
    }

    this.cardTransactionInstallmentRepository.saveAll(cardTransactionInstallmentList);

    cardTransaction.getTransactionInstallmentList().addAll(cardTransactionInstallmentList);
    activeCardStatement.getTransactionInstallmentList().addAll(cardTransactionInstallmentList);

    this.cardTransactionRepository.save(cardTransaction);

    // ATUALIZAR STATUS DA FATURA

    activeCardStatement.getTransactionList().add(cardTransaction);
    activeCardStatement.setTotalAmount(
        activeCardStatement.getTotalAmount() + data.totalPurchaseAmount()
    );

    this.cardStatementRepository.save(activeCardStatement);
    // SALVAR AS PARCELAS
    // SALVAR A TRANSACAO
    // ATUALIZAR FATURA
    return null;
  }

  private Card findOrCreateCardByAccountNumber(int accountNumber) {
    Optional<Card> cardOptional = this.cardRepository.findByAccountNumber(accountNumber);
    if (cardOptional.isPresent()) {
      return cardOptional.get();
    }

    Card newCard = new Card();
    newCard.setAccountNumber(accountNumber);

    CardStatement newCardStatement = new CardStatement();
    newCardStatement.setTotalAmount((double) 0);
    newCardStatement.setCard(newCard);
    newCardStatement.setStartedAt(LocalDateTime.now());
    newCardStatement.setStatus(ECardStatementStatus.OPEN);

    Card card = this.cardRepository.save(newCard);

    this.cardStatementRepository.save(newCardStatement);
    return card;
  }

  private void validateIfPurchaseDateIsValid(LocalDateTime purchaseDate) {
    LocalDateTime now = LocalDateTime.now().minusMinutes(3);
    if (purchaseDate.isBefore(now)) {
      throw new CustomException("The purchase date cannot be in the past.", HttpStatus.BAD_REQUEST);
    }
  }

  private void validateTotalInstallmentAmount(int numberOfInstallments, Double installmentAmount,
      Double totalPurchaseAmount) {
    if (installmentAmount > totalPurchaseAmount) {
      throw new CustomException("The total installment amount cannot exceed the purchase amount.",
          HttpStatus.BAD_REQUEST);
    }

    double totalAmount = numberOfInstallments * installmentAmount;
    if (totalAmount > totalPurchaseAmount) {
      throw new CustomException("The total installment amount cannot exceed the purchase amount.",
          HttpStatus.BAD_REQUEST);
    }
  }
}
