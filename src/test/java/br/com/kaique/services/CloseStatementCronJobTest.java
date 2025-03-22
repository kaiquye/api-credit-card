package br.com.kaique.services;

import br.com.kaique.entitys.CardStatement;
import br.com.kaique.entitys.ECardStatementStatus;
import br.com.kaique.helper.CreateTransactionHelper;
import br.com.kaique.repositories.CardStatementRepository;
import br.com.kaique.services.statement.close_statement_cron_job.CloseStatementJob;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class CloseStatementCronJobTest {

  @Inject()
  CloseStatementJob closeStatementJob;
  @Inject
  CreateTransactionHelper createTransactionHelper;
  @Inject
  CardStatementRepository statementRepository;

  @Test()
  void closeAllStatementByMonth() {
    var accountNumber = 1234567;
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);
    createTransactionHelper.execute(accountNumber);

    this.closeStatementJob.closePreviousMonthStatements();

    LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
        .atStartOfDay();
    LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
        .atTime(23, 59, 59);

    List<CardStatement> openStatements = this.statementRepository
        .findAllByDateRange(startOfMonth, endOfMonth);

    Assertions.assertTrue(openStatements.stream()
            .allMatch(statement -> statement.getStatus() == ECardStatementStatus.FINALIZED));
  }
}
