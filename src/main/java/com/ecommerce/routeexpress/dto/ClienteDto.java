package com.ecommerce.routeexpress.dto;

import jakarta.validation.constraints.*;

/**
 *
 * @author Daniel Arantes Telles
 */

public class ClienteDto {

	@NotEmpty(message = "O campo Nome é obrigatório")
	private String first_name;

	@NotEmpty(message = "O campo Sobrenome é obrigatório")
	private String last_name;

	@NotEmpty(message = "O campo Email é obrigatório")
	private String email;

	@NotEmpty(message = "O campo Número de Telefone é obrigatório")
	private String telefone;

	@NotEmpty(message = "O Campo CPF é obrigatório")
	private String cpf;

	private String senha;

	@NotEmpty(message = "O campo Gênero é obrigatório")
	private String sexo;

	@NotEmpty(message = "O campo Data de Nascimento é obrigatório")
	private String data_nascimento;

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(String data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

}
