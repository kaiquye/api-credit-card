package br.com.kaique.repositories;

import br.com.kaique.entitys.CardStatement;
import br.com.kaique.entitys.ECardStatementStatus;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

  @Query(
      "SELECT ct FROM CardStatement ct "
          + "WHERE ct.id = :cardStatementId AND ct.card.id = :cardId"
  )
  Optional<CardStatement> findStatementOpenedByIdAndCardId(Long cardStatementId,
      Long cardId);


  @Query(
      "SELECT ct FROM CardStatement ct "
          + "WHERE ct.card.id = :cardId "
          + "AND MONTH(ct.startedAt) = :month "
          + "AND YEAR(ct.startedAt) = :year"
  )
  Optional<CardStatement> findByCardIdAndMonth(Long cardId, int month, int year);

  @Query(
      "SELECT ct FROM CardStatement ct "
          + "WHERE ct.startedAt BETWEEN :startOfMonth AND :endOfMonth "
          + "AND ct.card.id = :cardId "
          + "AND ct.status = :status"
  )
  Optional<CardStatement> findStatementByStatusAndMonthAndCardId(
      LocalDateTime startOfMonth,
      LocalDateTime endOfMonth,
      ECardStatementStatus status,
      Long cardId
  );
}
