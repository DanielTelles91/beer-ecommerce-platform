package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.routeexpress.models.CarrinhoItem;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface CarrinhoItemRepositorio extends JpaRepository<CarrinhoItem, Long> {

}
