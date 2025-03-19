package br.com.kaique.entitys;

import br.com.kaique.common.EntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @ManyToOne
  @JoinColumn(name = "card_statements_id", nullable = false)
  private CardStatement statement;

  @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
  private List<CardTransactionInstallment> transactionInstallmentList = new ArrayList<>();
}
