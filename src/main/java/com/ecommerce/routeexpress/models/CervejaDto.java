package com.ecommerce.routeexpress.models;

import jakarta.validation.constraints.NotEmpty;

/**
*
* @author Daniel A. Telles
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
	
	//@NotEmpty(message = "The imagem_1 is required")
	//private String imagem_1;
	
	//@NotEmpty(message = "The imagem_2 is required")
	//private String imagem_2;
	
	//@NotEmpty(message = "The imagem_3 is required")
	//private String imagem_3;
	
	
	//***********************************
	private Integer cervejariaId;
	 
    public Integer getCervejariaId() {
	    return cervejariaId;
	}

	public void setCervejariaId(Integer cervejariaId) {
	    this.cervejariaId = cervejariaId;
	}
	//***********************************
	
	
	
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


	
	
	
}
