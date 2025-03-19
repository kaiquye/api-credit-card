package br.com.kaique.repositories;

import br.com.kaique.entitys.CardTransactionInstallment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface CardTransactionInstallmentRepository extends JpaRepository<CardTransactionInstallment, Long> {

}
