package com.ecommerce.routeexpress.dto;

import java.time.LocalDateTime;

/**
 *
 * @author Daniel Arantes Telles
 */

public class HistoricoStatusDto {
	private String status;
	private LocalDateTime dataMudanca;

	public HistoricoStatusDto(String status, LocalDateTime dataMudanca) {
		this.status = status;
		this.dataMudanca = dataMudanca;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getDataMudanca() {
		return dataMudanca;
	}
}
