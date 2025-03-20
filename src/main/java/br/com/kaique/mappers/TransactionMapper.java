package br.com.kaique.mappers;

import br.com.kaique.controllers.dtos.TransactionResponseDto;
import br.com.kaique.entitys.CardTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "jakarta")
public interface TransactionMapper {
  TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

  TransactionResponseDto toDTO(CardTransaction transaction);
}
