package br.com.kaique.entitys;

import br.com.kaique.common.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card_transaction_installment")
public class CardTransactionInstallment extends EntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "amount")
  private Double amount;

  @Column(name = "installment_number", nullable = false)
  private Integer installmentNumber;

  @ManyToOne
  @JoinColumn(name = "card_statement_id")
  private CardStatement statement;

  @ManyToOne
  @JoinColumn(name = "card_transaction_id")
  private CardTransaction transaction;
}
