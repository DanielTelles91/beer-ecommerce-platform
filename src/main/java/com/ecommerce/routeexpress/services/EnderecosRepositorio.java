package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Endereco;

/**
*
* @author Daniel A. Telles
*/

public interface EnderecosRepositorio extends JpaRepository<Endereco, Integer> {

	
}
