package br.com.creditCard.services.transaction.create_transaction;

import br.com.creditCard.common.UseCaseBase;
import br.com.creditCard.entitys.Card;

public interface CreateTransactionUseCase extends
    UseCaseBase<CreateTransactionInput, Card> {

}
