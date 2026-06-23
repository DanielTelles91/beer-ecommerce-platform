package com.ecommerce.routeexpress.dto;

/**
 *
 * @author Daniel Arantes Telles
 */

public class CarrinhoItemDto {
	private Long itemId;
	private int cervejaId;
	private String rotulo;
	private double preco;
	private int quantidade;
	private double subtotal;

	public CarrinhoItemDto(Long itemId, int cervejaId, String rotulo, double preco, int quantidade) {
		this.itemId = itemId;
		this.cervejaId = cervejaId;
		this.rotulo = rotulo;
		this.preco = preco;
		this.quantidade = quantidade;
		this.subtotal = preco * quantidade;
	}

	// getters
	public Long getItemId() {
		return itemId;
	}

	public int getCervejaId() {
		return cervejaId;
	}

	public String getRotulo() {
		return rotulo;
	}

	public double getPreco() {
		return preco;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public double getSubtotal() {
		return subtotal;
	}
}
