package br.com.creditCard.repositories;

import br.com.creditCard.entitys.CardTransaction;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

  Optional<CardTransaction> findByPurchaseDate(LocalDateTime purchaseDate);

  @Query("SELECT ct FROM CardTransaction ct JOIN ct.statements s WHERE s.id = :statementId")
  List<CardTransaction> findAllByStatementId(Long statementId);

}
