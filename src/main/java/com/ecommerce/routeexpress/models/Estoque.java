package com.ecommerce.routeexpress.models;

import jakarta.persistence.*;

/**
 *
 * @author Daniel Arantes Telles
 */

@Entity
@Table(name = "estoque")
public class Estoque {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "cerveja_id", nullable = false)
	private Cerveja cerveja;

	private int quantidade;
	private double porcentagemLucro;
	private int estoqueMinimo;
	private int estoqueMaximo;
	private double precoAquisicao;
	private double lucro;
	private double preco;
	private boolean disponibilidade;

	// getters e setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}

	public void setCerveja(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

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

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public boolean isDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(boolean disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	// opcional: calcular lucro automaticamente
	public void calcularLucro() {
		this.lucro = precoAquisicao * (porcentagemLucro / 100);
	}

	public void calcularPrecoFinal() {
		this.preco = lucro + precoAquisicao;
	}

}
