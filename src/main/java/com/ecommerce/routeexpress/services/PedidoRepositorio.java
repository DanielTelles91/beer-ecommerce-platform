package com.ecommerce.routeexpress.services;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.routeexpress.models.Pedido;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
	List<Pedido> findByClienteIdOrderByDataPedidoDesc(int clienteId);
}
