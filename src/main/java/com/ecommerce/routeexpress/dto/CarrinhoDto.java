package com.ecommerce.routeexpress.dto;

import java.util.List;

/**
 *
 * @author Daniel Arantes Telles
 */

public class CarrinhoDto {
	private List<CarrinhoItemDto> itens;
	private double total;
	private int totalItens;

	public CarrinhoDto(List<CarrinhoItemDto> itens) {
		this.itens = itens;
		this.total = itens.stream().mapToDouble(CarrinhoItemDto::getSubtotal).sum();
		this.totalItens = itens.stream().mapToInt(CarrinhoItemDto::getQuantidade).sum();
	}

	public List<CarrinhoItemDto> getItens() {
		return itens;
	}

	public double getTotal() {
		return total;
	}

	public int getTotalItens() {
		return totalItens;
	}
}
