package com.ecommerce.routeexpress.models;

import jakarta.persistence.*;

/**
 *
 * @author Daniel Arantes Telles
 */

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;

	// Referência fraca — nullable, pois a cerveja pode ser deletada no futuro!
	@Column(name = "cerveja_id")
	private Integer cervejaId;

	@Column(name = "cervejaria_id_snapshot")
	private Integer cervejariaId;

	// Snapshots — dados salvos no momento da compra
	private String rotulo;
	private String nomeCervejaria;
	private double precoUnitario;
	private String imagem;
	private int quantidade;
	private double subtotal;

	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
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
