package br.com.creditCard.services.transaction.list_transaction;

import br.com.creditCard.entitys.CardTransaction;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Serdeable
@Getter
@Setter
@RequiredArgsConstructor
public class ListTransactionOutput {

  private String companyName;
  private int installmentsCount;
  private double installmentsAmount;
  private int installmentNumber;
  private double totalPurchaseAmount;
  private LocalDateTime purchaseDate;

  public ListTransactionOutput(CardTransaction transaction, int numberOfCurrentInstallment) {
    this.companyName = transaction.getCompanyName();
    this.installmentsCount = transaction.getInstallmentsCount();
    this.installmentsAmount = transaction.getInstallmentsAmount();
    this.installmentNumber = numberOfCurrentInstallment;
    this.totalPurchaseAmount = transaction.getTotalPurchaseAmount();
    this.purchaseDate = transaction.getPurchaseDate();
  }
}
