package com.ecommerce.routeexpress.dto;

import java.time.LocalDateTime;

/**
 *
 * @author Daniel Arantes Telles
 */

public class ListaDeDesejosDto {
	private int cervejaId;
	private String rotulo;
	private String cervejaria;
	private int cervejariaId;
	private String imagem;
	private double preco;
	private LocalDateTime dataAdicao;
	private boolean disponivel;

	// getters e setters
	public int getCervejaId() {
		return cervejaId;
	}

	public void setCervejaId(int cervejaId) {
		this.cervejaId = cervejaId;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getCervejaria() {
		return cervejaria;
	}

	public void setCervejaria(String cervejaria) {
		this.cervejaria = cervejaria;
	}

	public int getCervejariaId() {
		return cervejariaId;
	}

	public void setCervejariaId(int cervejariaId) {
		this.cervejariaId = cervejariaId;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public LocalDateTime getDataAdicao() {
		return dataAdicao;
	}

	public void setDataAdicao(LocalDateTime dataAdicao) {
		this.dataAdicao = dataAdicao;
	}

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}
}
