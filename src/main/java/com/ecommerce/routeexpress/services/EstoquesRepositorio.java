package com.ecommerce.routeexpress.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.routeexpress.models.Estoque;

import jakarta.persistence.LockModeType;
/**
 *
 * @author Daniel Arantes Telles
 */

public interface EstoquesRepositorio extends JpaRepository<Estoque, Integer> {

	@Query("select e.cerveja.id from Estoque e")
	List<Integer> findAllCervejaIdsEmEstoque();

	boolean existsByCervejaId(int cervejaId);

	Optional<Estoque> findFirstByCervejaId(int cervejaId); // carrinho

	// Trava a linha no banco (lockpessimista) até a transação terminar. 
	// Usado no checkout, onde precisa garantir que nenhuma outra 
	// transação leia/altere esse mesmo estoque enquanto está validando e debitando.
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select e from Estoque e where e.cerveja.id = :cervejaId")
	Optional<Estoque> findFirstByCervejaIdForUpdate(@Param("cervejaId") int cervejaId);

}
