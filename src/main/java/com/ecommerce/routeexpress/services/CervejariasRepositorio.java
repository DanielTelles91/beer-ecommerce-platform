package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Cervejaria;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface CervejariasRepositorio extends JpaRepository<Cervejaria, Integer> {
	boolean existsByCervejariaIgnoreCase(String cervejaria);

	boolean existsByCervejariaAndIdNot(String cervejaria, int id);

}
