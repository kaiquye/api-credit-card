package br.com.creditCard.repositories;

import br.com.creditCard.entitys.Card;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

  Optional<Card> findByAccountNumber(Integer accountNumber);
}
