package com.ecommerce.routeexpress.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Endereco;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface EnderecosRepositorio extends JpaRepository<Endereco, Integer> {

	List<Endereco> findByClienteId(int clienteId);

}
