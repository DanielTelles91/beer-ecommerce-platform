package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Cliente;

public interface ClientesRepositorio extends JpaRepository<Cliente, Integer> {

}
