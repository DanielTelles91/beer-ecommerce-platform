package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.ListaDeDesejos;

/**
 *
 * @author Daniel A. Telles
 */

public interface ListaDeDesejosRepositorio extends JpaRepository<ListaDeDesejos, Integer> {

	void deleteByClienteIdAndCervejaId(int clienteId, int cervejaId);

}
