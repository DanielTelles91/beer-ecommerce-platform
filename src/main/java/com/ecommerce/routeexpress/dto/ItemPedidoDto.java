package com.ecommerce.routeexpress.dto;

/**
 *
 * @author Daniel Arantes Telles
 */

public class ItemPedidoDto {
	private Long id;
	private Integer cervejaId;
	private String rotulo;
	private String nomeCervejaria;
	private double precoUnitario;
	private String imagem;
	private int quantidade;
	private double subtotal;
	private Integer cervejariaId;

	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCervejaId() {
		return cervejaId;
	}

	public void setCervejaId(Integer cervejaId) {
		this.cervejaId = cervejaId;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getNomeCervejaria() {
		return nomeCervejaria;
	}

	public void setNomeCervejaria(String nomeCervejaria) {
		this.nomeCervejaria = nomeCervejaria;
	}

	public double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Integer getCervejariaId() {
		return cervejariaId;
	}

	public void setCervejariaId(Integer cervejariaId) {
		this.cervejariaId = cervejariaId;
	}
}
