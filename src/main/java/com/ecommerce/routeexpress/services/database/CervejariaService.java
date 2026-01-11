package com.ecommerce.routeexpress.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.dto.CervejariaDto;
import com.ecommerce.routeexpress.exceptions.CervejaJaExisteException;
import com.ecommerce.routeexpress.models.Cervejaria;
import com.ecommerce.routeexpress.services.CervejariasRepositorio;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service

public class CervejariaService {

	@Autowired
	private CervejariasRepositorio cervejariasRepositorio;

	public Cervejaria criaCervejaria(CervejariaDto cervejariaDto) { // Creates a new Cervejaria

		if (cervejariasRepositorio.existsByCervejariaIgnoreCase(cervejariaDto.getCervejaria())) {
			throw new CervejaJaExisteException("Já existe uma Cervejaria com esse nome !");
		}

		Cervejaria cervejaria = new Cervejaria();
		cervejaria.setCervejaria(cervejariaDto.getCervejaria());
		cervejaria.setPais(cervejariaDto.getPais());

		return cervejariasRepositorio.save(cervejaria); // Save in database

	}

	public Cervejaria updateCervejaria(int id, CervejariaDto dto) { // Updates a cervejaria based on the DTO
		Cervejaria cervejaria = cervejariasRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));

		if (cervejariasRepositorio.existsByCervejariaAndIdNot(dto.getCervejaria(), id)) {
			throw new CervejaJaExisteException("Já existe uma Cervejaria com esse nome");
		}

		cervejaria.setCervejaria(dto.getCervejaria());
		cervejaria.setPais(dto.getPais());

		return cervejariasRepositorio.save(cervejaria); // Save in database
	}

	public void deleteCervejariaById(int id) { // Deleta cervejaria
		Cervejaria cervejaria = cervejariasRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));
		cervejariasRepositorio.delete(cervejaria);
	}

	public Cervejaria findById(int id) { // Find Cervejaria by ID or throw RuntimeException if not found
		return cervejariasRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));
	}

}
