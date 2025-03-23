package br.com.creditCard.entitys;

import br.com.creditCard.common.EntityBase;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Serdeable
@Table(name = "card_transaction_installment")
public class CardTransactionInstallment extends EntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "amount")
  private Double amount;

  @Column(name = "installment_number", nullable = false)
  private Integer installmentNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "card_statement_id")
  private CardStatement statement;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "card_transaction_id")
  private CardTransaction transaction;
}
