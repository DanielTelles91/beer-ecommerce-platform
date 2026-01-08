package com.ecommerce.routeexpress.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.routeexpress.models.Estoque;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface EstoquesRepositorio extends JpaRepository<Estoque, Integer> {

	@Query("select e.cerveja.id from Estoque e")
	List<Integer> findAllCervejaIdsEmEstoque();

	boolean existsByCervejaId(int cervejaId);

}
