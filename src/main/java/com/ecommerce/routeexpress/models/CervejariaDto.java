package com.ecommerce.routeexpress.models;

import jakarta.validation.constraints.NotEmpty;

/**
*
* @author Daniel A. Telles
*/


public class CervejariaDto {
	
	@NotEmpty(message = "The Cervejaria is required")
	private String cervejaria;
	
	@NotEmpty(message = "The pais is required")
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
