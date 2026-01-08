package com.ecommerce.routeexpress.dto;

import org.springframework.web.multipart.*;

import jakarta.validation.constraints.NotEmpty;


/**
 *
 * @author Daniel Arantes Telles
 */

public class CervejaDto {

	@NotEmpty(message = "The rotulo is required")
	private String rotulo;

	@NotEmpty(message = "The preco is required")
	private String preco;

	@NotEmpty(message = "The volume is required")
	private String volume;

	@NotEmpty(message = "The teor is required")
	private String teor;

	@NotEmpty(message = "The cor is required")
	private String cor;

	@NotEmpty(message = "The temperatura is required")
	private String temperatura;

	@NotEmpty(message = "The familia_e_estilo is required")
	private String familia_e_estilo;

	@NotEmpty(message = "The descricao is required")
	private String descricao;

	@NotEmpty(message = "The sabor is required")
	private String sabor;

	private MultipartFile imagem_1;

	private MultipartFile imagem_2;

	private MultipartFile imagem_3;

	// ***********************************
	private Integer cervejariaId;

	public Integer getCervejariaId() {
		return cervejariaId;
	}

	public void setCervejariaId(Integer cervejariaId) {
		this.cervejariaId = cervejariaId;
	}
	// ***********************************

	public String getRotulo() {
		return rotulo;
	}

	public MultipartFile getImagem_1() {
		return imagem_1;
	}

	public void setImagem_1(MultipartFile imagem_1) {
		this.imagem_1 = imagem_1;
	}

	public MultipartFile getImagem_2() {
		return imagem_2;
	}

	public void setImagem_2(MultipartFile imagem_2) {
		this.imagem_2 = imagem_2;
	}

	public MultipartFile getImagem_3() {
		return imagem_3;
	}

	public void setImagem_3(MultipartFile imagem_3) {
		this.imagem_3 = imagem_3;
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

}
