package com.ecommerce.routeexpress.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author Daniel Arantes Telles
 */

public class CervejariaDto {

	@NotEmpty(message = "O campo Cervejaria é obrigatório")
	private String cervejaria;

	@NotEmpty(message = "O campo Pais é obrigatório")
	private String pais;

	public String getCervejaria() {
		return cervejaria;
	}

	public void setCervejaria(String cervejaria) {
		this.cervejaria = cervejaria;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

}
