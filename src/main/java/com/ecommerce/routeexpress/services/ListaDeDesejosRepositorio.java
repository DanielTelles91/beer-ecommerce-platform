package com.ecommerce.routeexpress.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.ListaDeDesejos;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface ListaDeDesejosRepositorio extends JpaRepository<ListaDeDesejos, Integer> {
	void deleteByClienteIdAndCervejaId(int clienteId, int cervejaId);

	List<ListaDeDesejos> findByClienteId(int clienteId);

	boolean existsByClienteIdAndCervejaId(int clienteId, int cervejaId);
}
