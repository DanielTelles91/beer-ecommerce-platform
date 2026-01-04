package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Cervejaria;

/**
*
* @author Daniel A. Telles
*/

public interface CervejariasRepositorio extends JpaRepository<Cervejaria, Integer> {

}
