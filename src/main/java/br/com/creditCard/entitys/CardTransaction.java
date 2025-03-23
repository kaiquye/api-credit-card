package br.com.creditCard.entitys;

import br.com.creditCard.common.EntityBase;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Serdeable
@Table(name = "card_transaction")
public class CardTransaction extends EntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "company_name", nullable = false)
  private String companyName;

  @Column(name = "installments_count", nullable = false)
  private Integer installmentsCount;

  @Column(name = "installments_amount", nullable = false)
  private Double installmentsAmount;

  @Column(name = "total_purchase_amount", nullable = false)
  private Double totalPurchaseAmount;

  @Column(name = "purchase_date")
  private LocalDateTime purchaseDate;

  @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<CardTransactionInstallment> transactionInstallmentList = new ArrayList<>();

  @ManyToMany(mappedBy = "transactions", fetch = FetchType.EAGER)
  private List<CardStatement> statements = new ArrayList<>();
}
