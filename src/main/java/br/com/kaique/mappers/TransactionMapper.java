package br.com.kaique.mappers;

import br.com.kaique.controllers.dtos.TransactionResponseDto;
import br.com.kaique.services.transaction.list_transaction.ListTransactionOutput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "jakarta")
public interface TransactionMapper {
  TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

  TransactionResponseDto toDTO(ListTransactionOutput transaction);
}
