package br.com.kaique.repositories;

import br.com.kaique.entitys.CardTransactionInstallment;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface CardTransactionInstallmentRepository extends
    JpaRepository<CardTransactionInstallment, Long> {

  @Query("SELECT cti FROM CardTransactionInstallment cti " +
      "WHERE cti.transaction.id = :transactionId AND cti.statement.id = :cardId")
  Optional<CardTransactionInstallment> findByTransactionIdAndCardId(Long transactionId,
      Long cardId);

  @Query("SELECT cti FROM CardTransactionInstallment cti WHERE cti.statement.card.id = :cardId ORDER BY cti.id DESC LIMIT 1")
  Optional<CardTransactionInstallment> findLastByCardId(Long cardId);
}
