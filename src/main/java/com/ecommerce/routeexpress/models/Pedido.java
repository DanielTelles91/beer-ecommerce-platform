package com.ecommerce.routeexpress.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 *
 * @author Daniel Arantes Telles
 */

@Entity
@Table(name = "pedido")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cliente_id", nullable = false)
	private int clienteId;

	private LocalDateTime dataPedido = LocalDateTime.now();

	private String status = "CONFIRMADO";

	private double total;

	// Snapshot do endereço no momento da compra
	private String enderecoLogradouro;
	private String enderecoTipoLogradouro;
	private String enderecoNumero;
	private String enderecoComplemento;
	private String enderecoBairro;
	private String enderecoCidade;
	private String enderecoEstado;
	private String enderecoCep;

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemPedido> itens = new ArrayList<>();

	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getClienteId() {
		return clienteId;
	}

	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
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

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	public String getEnderecoLogradouro() {
		return enderecoLogradouro;
	}

	public void setEnderecoLogradouro(String enderecoLogradouro) {
		this.enderecoLogradouro = enderecoLogradouro;
	}

	public String getEnderecoTipoLogradouro() {
		return enderecoTipoLogradouro;
	}

	public void setEnderecoTipoLogradouro(String enderecoTipoLogradouro) {
		this.enderecoTipoLogradouro = enderecoTipoLogradouro;
	}

	public String getEnderecoNumero() {
		return enderecoNumero;
	}

	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}

	public String getEnderecoComplemento() {
		return enderecoComplemento;
	}

	public void setEnderecoComplemento(String enderecoComplemento) {
		this.enderecoComplemento = enderecoComplemento;
	}

	public String getEnderecoBairro() {
		return enderecoBairro;
	}

	public void setEnderecoBairro(String enderecoBairro) {
		this.enderecoBairro = enderecoBairro;
	}

	public String getEnderecoCidade() {
		return enderecoCidade;
	}

	public void setEnderecoCidade(String enderecoCidade) {
		this.enderecoCidade = enderecoCidade;
	}

	public String getEnderecoEstado() {
		return enderecoEstado;
	}

	public void setEnderecoEstado(String enderecoEstado) {
		this.enderecoEstado = enderecoEstado;
	}

	public String getEnderecoCep() {
		return enderecoCep;
	}

	public void setEnderecoCep(String enderecoCep) {
		this.enderecoCep = enderecoCep;
	}
}
