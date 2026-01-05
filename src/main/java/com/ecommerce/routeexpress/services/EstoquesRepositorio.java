package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Estoque;

public interface EstoquesRepositorio extends JpaRepository<Estoque, Integer> {
	// métodos customizados se precisar
}
