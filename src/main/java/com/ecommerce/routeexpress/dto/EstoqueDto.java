package com.ecommerce.routeexpress.dto;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Daniel Arantes Telles
 */

public class EstoqueDto {

	@NotNull(message = "The Quantidade is required")
	private int quantidade;

	@NotNull(message = "The porcentagem de lucro is required")
	private double porcentagemLucro;

	@NotNull(message = "The Estoque Minimo is required")
	private int estoqueMinimo;

	@NotNull(message = "The Estoque Máximo is required")
	private int estoqueMaximo;

	@NotNull(message = "The Preço de Aquisição is required")
	private double precoAquisicao;

	@NotNull(message = "The Lucro is required")
	private double lucro;

	@NotNull(message = "The Disponibilidade is required")
	private boolean disponibilidade;

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPorcentagemLucro() {
		return porcentagemLucro;
	}

	public void setPorcentagemLucro(double porcentagemLucro) {
		this.porcentagemLucro = porcentagemLucro;
	}

	public int getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(int estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public int getEstoqueMaximo() {
		return estoqueMaximo;
	}

	public void setEstoqueMaximo(int estoqueMaximo) {
		this.estoqueMaximo = estoqueMaximo;
	}

	public double getPrecoAquisicao() {
		return precoAquisicao;
	}

	public void setPrecoAquisicao(double precoAquisicao) {
		this.precoAquisicao = precoAquisicao;
	}

	public double getLucro() {
		return lucro;
	}

	public void setLucro(double lucro) {
		this.lucro = lucro;
	}

	public boolean isDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(boolean disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

}
