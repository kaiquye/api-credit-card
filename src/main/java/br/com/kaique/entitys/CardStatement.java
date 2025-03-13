package br.com.kaique.entitys;

import br.com.kaique.common.ECardStatementStatus;
import br.com.kaique.common.EntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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

  @Column(name = "ended_at", nullable = false)
  private LocalDateTime endedAt;

  @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CardTransaction> transactionList;

  @ManyToOne
  @JoinColumn(name = "credit_card_id", nullable = false)
  private Card card;

  @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CardTransactionInstallment> transactionInstallmentList;
}
