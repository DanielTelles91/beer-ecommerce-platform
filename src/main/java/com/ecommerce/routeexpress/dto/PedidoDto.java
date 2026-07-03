package com.ecommerce.routeexpress.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Daniel Arantes Telles
 */

public class PedidoDto {
	private Long id;
	private LocalDateTime dataPedido;
	private String status;
	private double total;
	private String enderecoCompleto;
	private List<ItemPedidoDto> itens;

	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDateTime dataPedido) {
		this.dataPedido = dataPedido;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getEnderecoCompleto() {
		return enderecoCompleto;
	}

	public void setEnderecoCompleto(String enderecoCompleto) {
		this.enderecoCompleto = enderecoCompleto;
	}

	public List<ItemPedidoDto> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedidoDto> itens) {
		this.itens = itens;
	}
}
