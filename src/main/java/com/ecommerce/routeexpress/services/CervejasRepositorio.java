package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Cerveja;

/**
*
* @author Daniel Arantes Telles
*/

public interface CervejasRepositorio extends JpaRepository<Cerveja, Integer>{

	 boolean existsByRotuloIgnoreCase(String rotulo);
	 
	 boolean existsByRotuloAndIdNot(String Rotulo, int id);
}
