package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Cerveja;

public interface CervejasRepositorio extends JpaRepository<Cerveja, Integer>{

}
