package com.ecommerce.routeexpress.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 *
 * @author Daniel Arantes Telles
 */

@Entity
@Table(name = "pedido_status_historico")
public class PedidoStatusHistorico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "pedido_id", nullable = false)
	private Pedido pedido;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private LocalDateTime dataMudanca;

	public PedidoStatusHistorico() {
	}

	public PedidoStatusHistorico(Pedido pedido, String status) {
		this.pedido = pedido;
		this.status = status;
		this.dataMudanca = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getDataMudanca() {
		return dataMudanca;
	}
}
