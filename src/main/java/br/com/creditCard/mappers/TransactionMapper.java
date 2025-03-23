package br.com.creditCard.mappers;

import br.com.creditCard.controllers.dtos.TransactionResponseDto;
import br.com.creditCard.services.transaction.list_transaction.ListTransactionOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface TransactionMapper {
  //TransactionResponseDto toDTO(ListTransactionOutput transaction);
}
