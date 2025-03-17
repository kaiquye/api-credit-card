package br.com.kaique.repositories;

import br.com.kaique.entitys.CardTransaction;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

  Optional<CardTransaction> findByPurchaseDate(LocalDateTime purchaseDate);
}
