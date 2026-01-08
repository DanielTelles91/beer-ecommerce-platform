package com.ecommerce.routeexpress.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author Daniel Arantes Telles
 */

@Entity
@Table(name = "cerveja")
public class Cerveja {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "cervejaria_id", nullable = false)
	private Cervejaria cervejaria;

	@OneToMany(mappedBy = "cerveja", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ListaDeDesejos> listaDesejos = new ArrayList<>();
	// Relação com a lista de desejos: quando uma cerveja é removida,
	// todos os itens relacionados nesta lista de desejos também são removidos
	// automaticamente.

	@OneToMany(mappedBy = "cerveja", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Estoque> estoques = new ArrayList<>();
	// Relação com a estoques: quando uma cerveja é removida,
	// todos os itens relacionados a este estoque também são removidos
	// automaticamente.

	public void setCervejaria(Cervejaria cervejaria) {
		this.cervejaria = cervejaria;
	}
	
	@Column(nullable = false, unique = true)
	private String rotulo;
	
	private String preco;
	private String volume;
	private String teor;
	private String cor;
	private String temperatura;
	private String familia_e_estilo;
	private String descricao;
	private String sabor;

	@Column(columnDefinition = "TEXT")
	private String imagem_1;
	private String imagem_2;
	private String imagem_3;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getTeor() {
		return teor;
	}

	public void setTeor(String teor) {
		this.teor = teor;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public String getFamilia_e_estilo() {
		return familia_e_estilo;
	}

	public void setFamilia_e_estilo(String familia_e_estilo) {
		this.familia_e_estilo = familia_e_estilo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSabor() {
		return sabor;
	}

	public void setSabor(String sabor) {
		this.sabor = sabor;
	}

	public String getImagem_1() {
		return imagem_1;
	}

	public void setImagem_1(String imagem_1) {
		this.imagem_1 = imagem_1;
	}

	public String getImagem_2() {
		return imagem_2;
	}

	public void setImagem_2(String imagem_2) {
		this.imagem_2 = imagem_2;
	}

	public String getImagem_3() {
		return imagem_3;
	}

	public void setImagem_3(String imagem_3) {
		this.imagem_3 = imagem_3;
	}

	public Cervejaria getCervejaria() {
		return cervejaria;
	}

}
