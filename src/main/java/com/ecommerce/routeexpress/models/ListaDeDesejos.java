package com.ecommerce.routeexpress.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 *
 * @author Daniel Arantes Telles
 */

@Entity
@Table(name = "lista_desejo", uniqueConstraints = { @UniqueConstraint(columnNames = { "cliente_id", "cerveja_id" }) })
public class ListaDeDesejos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "cerveja_id", nullable = false)
	private Cerveja cerveja;
	
	private LocalDateTime dataAdicao = LocalDateTime.now();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}

	public void setCerveja(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

	public LocalDateTime getDataAdicao() {
		return dataAdicao;
	}

	public void setDataAdicao(LocalDateTime dataAdicao) {
		this.dataAdicao = dataAdicao;
	}
	
	
}
