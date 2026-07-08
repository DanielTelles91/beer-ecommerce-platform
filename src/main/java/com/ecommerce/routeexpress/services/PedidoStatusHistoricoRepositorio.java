package com.ecommerce.routeexpress.services;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.routeexpress.models.PedidoStatusHistorico;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface PedidoStatusHistoricoRepositorio extends JpaRepository<PedidoStatusHistorico, Long> {
	List<PedidoStatusHistorico> findByPedidoIdOrderByDataMudancaAsc(Long pedidoId);
}
