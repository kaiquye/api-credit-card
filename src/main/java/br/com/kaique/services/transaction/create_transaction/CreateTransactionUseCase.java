package br.com.kaique.services.transaction.create_transaction;

import br.com.kaique.common.UseCaseBase;
import br.com.kaique.entitys.Card;

public interface CreateTransactionUseCase extends
    UseCaseBase<CreateTransactionInput, Card> {

}
