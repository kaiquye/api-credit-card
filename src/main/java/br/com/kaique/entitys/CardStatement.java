package br.com.kaique.entitys;

import br.com.kaique.common.EntityBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
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
@Table(name = "card_statement")
@Serdeable
public class CardStatement extends EntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ECardStatementStatus status = ECardStatementStatus.OPEN;

  @Column(name = "total_amount", nullable = false)
  private Double totalAmount;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "due_date", nullable = true)
  private LocalDateTime dueDate;

  @ManyToMany
  @JoinTable(
      name = "card_statement_transaction",
      joinColumns = @JoinColumn(name = "card_statement_id"),
      inverseJoinColumns = @JoinColumn(name = "card_transaction_id")
  )
  private List<CardTransaction> transactions = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "credit_card_id", nullable = false)
  private Card card;

  @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<CardTransactionInstallment> transactionInstallmentList = new ArrayList<>();
}
