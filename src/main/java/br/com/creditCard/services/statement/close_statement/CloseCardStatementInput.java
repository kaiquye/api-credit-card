package br.com.creditCard.services.statement.close_statement;

import lombok.Builder;

@Builder
public record CloseCardStatementInput(int accountNumber, Long statementId) {

}
