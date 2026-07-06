package com.ecommerce.routeexpress.services;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.routeexpress.models.Pedido;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
	List<Pedido> findByClienteIdOrderByDataPedidoDesc(int clienteId);

	// lista todos os pedidos ordenados por data (admin)
	List<Pedido> findAllByOrderByDataPedidoDesc();

	// filtra por status
	List<Pedido> findByStatusOrderByDataPedidoDesc(String status);

	// total de vendas de um ano (por mês)
	@Query(value = """
			SELECT MONTH(data_pedido) as mes, SUM(total) as total
			FROM pedido
			WHERE YEAR(data_pedido) = :ano
			AND status != 'CANCELADO'
			GROUP BY MONTH(data_pedido)
			ORDER BY mes
			""", nativeQuery = true)
	List<Object[]> vendasPorMes(@Param("ano") int ano);

	// total de pedidos por status
	@Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status")
	List<Object[]> contagemPorStatus();

	// top 5 cervejas filtrado por ano
	@Query(value = """
			SELECT ip.rotulo, SUM(ip.quantidade) as total_vendido
			FROM item_pedido ip
			JOIN pedido p ON ip.pedido_id = p.id
			WHERE p.status != 'CANCELADO'
			AND YEAR(p.data_pedido) = :ano
			GROUP BY ip.rotulo
			ORDER BY total_vendido DESC
			LIMIT 5
			""", nativeQuery = true)
	List<Object[]> topCervejas(@Param("ano") int ano);

	// contagem por status filtrado por ano
	@Query(value = """
			SELECT status, COUNT(*) as total
			FROM pedido
			WHERE YEAR(data_pedido) = :ano
			GROUP BY status
			""", nativeQuery = true)
	List<Object[]> contagemPorStatus(@Param("ano") int ano);

	// total de vendas do ano
	@Query(value = """
			SELECT COALESCE(SUM(total), 0)
			FROM pedido
			WHERE YEAR(data_pedido) = :ano
			AND status != 'CANCELADO'
			""", nativeQuery = true)
	Double totalVendasAno(@Param("ano") int ano);

	// total de pedidos do ano
	@Query(value = """
			SELECT COUNT(*)
			FROM pedido
			WHERE YEAR(data_pedido) = :ano
			AND status != 'CANCELADO'
			""", nativeQuery = true)
	Long totalPedidosAno(@Param("ano") int ano);
}
