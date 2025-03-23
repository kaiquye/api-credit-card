package br.com.creditCard.services.statement.close_statement_cron_job.impl;

import br.com.creditCard.entitys.CardStatement;
import br.com.creditCard.entitys.ECardStatementStatus;
import br.com.creditCard.repositories.CardStatementRepository;
import br.com.creditCard.services.statement.close_statement_cron_job.CloseStatementJob;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Singleton
public class CloseStatementJobImpl implements CloseStatementJob {

  private final CardStatementRepository cardStatementRepository;

  @Scheduled(cron = "0 0 2 1 * ?")
  public void closePreviousMonthStatements() {
    System.out.println("Fechamento automático de faturas...");

    LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

    List<CardStatement> openStatements = cardStatementRepository.findAllByStatusAndDateRange(
        ECardStatementStatus.OPEN, startOfMonth, endOfMonth);

    if (openStatements.isEmpty()) {
      System.out.println("Nenhuma fatura aberta encontrada para fechamento.");
      return;
    }

    openStatements.forEach(statement -> statement.setStatus(ECardStatementStatus.FINALIZED));
    this.cardStatementRepository.updateAll(openStatements);

    System.out.println("Fechamento concluído: " + openStatements.size() + " faturas finalizadas.");
  }
}
