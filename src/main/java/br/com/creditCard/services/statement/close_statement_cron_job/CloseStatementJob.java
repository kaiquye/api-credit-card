package br.com.creditCard.services.statement.close_statement_cron_job;

public interface CloseStatementJob {

  void closePreviousMonthStatements();
}
