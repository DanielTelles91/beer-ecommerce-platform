package com.ecommerce.routeexpress.dto;

/**
 *
 * @author Daniel Arantes Telles
 */

public class EnderecoResponseDto {

	private int id;
	private String cep;
	private String logradouro;
	private String tipo_logradouro;
	private String logradouro_numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;

	// getters e setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getTipo_logradouro() {
		return tipo_logradouro;
	}

	public void setTipo_logradouro(String tipo_logradouro) {
		this.tipo_logradouro = tipo_logradouro;
	}

	public String getLogradouro_numero() {
		return logradouro_numero;
	}

	public void setLogradouro_numero(String logradouro_numero) {
		this.logradouro_numero = logradouro_numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
