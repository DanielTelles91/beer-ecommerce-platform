package com.ecommerce.routeexpress.models;

import jakarta.persistence.*;

/**
 *
 * @author Daniel Arantes Telles
 */

@Entity
@Table(name = "carrinho_item")
public class CarrinhoItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "carrinho_id")
	private Carrinho carrinho;

	@ManyToOne
	@JoinColumn(name = "cerveja_id")
	private Cerveja cerveja;

	@Column(nullable = false)
	private int quantidade;

	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Carrinho getCarrinho() {
		return carrinho;
	}

	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
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
}
