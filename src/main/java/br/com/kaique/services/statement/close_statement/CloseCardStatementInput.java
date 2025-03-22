package br.com.kaique.services.statement.close_statement;

import lombok.Builder;

@Builder
public record CloseCardStatementInput(int accountNumber, Long statementId) {

}
