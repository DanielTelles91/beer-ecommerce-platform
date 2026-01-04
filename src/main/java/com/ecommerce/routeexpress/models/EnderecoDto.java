package com.ecommerce.routeexpress.models;

import jakarta.validation.constraints.*;

/**
 *
 * @author Daniel A. Telles
 */

public class EnderecoDto {

	@NotEmpty(message = "The CEP is required")
	private String cep;

	@NotEmpty(message = "The Logradouro is required")
	private String logradouro;

	@NotEmpty(message = "The Tipo Logradouro is required")
	private String tipo_logradouro;

	@NotEmpty(message = "The Logradouro Numero is required")
	private String logradouro_numero;

	private String complemento;

	@NotEmpty(message = "The Bairro is required")
	private String bairro;

	@NotEmpty(message = "The Cidade is required")
	private String cidade;

	@NotEmpty(message = "The Estado is required")
	private String estado;

	@NotEmpty(message = "The CPF is required")
	private String cpf;

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}
