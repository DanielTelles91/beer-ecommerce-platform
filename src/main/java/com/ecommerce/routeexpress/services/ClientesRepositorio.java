package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Cliente;

/**
*
* @author Daniel A. Telles
*/

public interface ClientesRepositorio extends JpaRepository<Cliente, Integer> {
	 Cliente findByCpf(String cpf); // permite buscar cliente pelo CPF
}