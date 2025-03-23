package br.com.creditCard.services.transaction.list_transaction;

import br.com.creditCard.entitys.CardTransaction;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ListTransactionOutput {

  private Long id;
  private String companyName;
  private Integer installmentsCount;
  private Double installmentsAmount;
  private int currentInstallmentsNumber;
  private Double totalPurchaseAmount;
  private LocalDateTime purchaseDate;

  public ListTransactionOutput(CardTransaction transaction, int numberOfCurrentInstallment) {
    this.id = transaction.getId();
    this.companyName = transaction.getCompanyName();
    this.installmentsCount = transaction.getInstallmentsCount();
    this.installmentsAmount = transaction.getInstallmentsAmount();
    this.currentInstallmentsNumber = numberOfCurrentInstallment;
    this.totalPurchaseAmount = transaction.getTotalPurchaseAmount();
    this.purchaseDate = transaction.getPurchaseDate();
  }
}
