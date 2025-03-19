package br.com.kaique.repositories;

import br.com.kaique.entitys.CardStatement;
import br.com.kaique.entitys.ECardStatementStatus;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardStatementRepository extends JpaRepository<CardStatement, Long> {

  @Query(
      "SELECT ct FROM CardStatement ct "
          + "WHERE ct.status = 'OPEN' AND ct.card.id = :cardId"
  )
  Optional<CardStatement> findStatementOpenedByCardId(Long cardId);


  List<CardStatement> findByStatusInAndCardIdOrderByStartedAt(List<ECardStatementStatus> status,
      Long cardId);
}
