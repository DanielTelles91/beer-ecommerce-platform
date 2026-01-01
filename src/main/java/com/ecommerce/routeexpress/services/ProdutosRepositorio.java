package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Product;


public interface ProdutosRepositorio extends JpaRepository<Product, Integer> {

}
