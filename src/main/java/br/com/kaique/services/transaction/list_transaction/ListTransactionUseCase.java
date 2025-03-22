package br.com.kaique.services.transaction.list_transaction;

import br.com.kaique.common.UseCaseBase;
import br.com.kaique.entitys.CardStatement;
import br.com.kaique.entitys.CardTransaction;
import java.util.List;

public interface ListTransactionUseCase extends
    UseCaseBase<ListTransactionInput, List<ListTransactionOutput>> {

}
