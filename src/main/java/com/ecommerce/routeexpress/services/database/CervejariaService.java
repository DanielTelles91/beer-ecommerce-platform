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
	private CervejariasRepositorio repo;

	@Autowired
	private CervejariasRepositorio cervejariasRepositorio;

	// Deleta cervejaria
	public void deleteCervejariaById(int id) {
		Cervejaria cervejaria = repo.findById(id).orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));
		repo.delete(cervejaria);
	}

	// Cria Cervejaria
	public Cervejaria criaCervejaria(CervejariaDto cervejariaDto) {

		if (cervejariasRepositorio.existsByCervejariaIgnoreCase(cervejariaDto.getCervejaria())) {
			throw new CervejaJaExisteException("Já existe uma Cervejaria com esse nome !");
		}

		Cervejaria cervejaria = new Cervejaria();
		cervejaria.setCervejaria(cervejariaDto.getCervejaria());
		cervejaria.setPais(cervejariaDto.getPais());

		return repo.save(cervejaria); // Salva no BD

	}

	// Busca cervejaria pelo ID ou lança RuntimeException se não existir
	public Cervejaria findById(int id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));
	}

	// Atualiza cervejaria existente com dados do DTO
	public Cervejaria updateCervejaria(int id, CervejariaDto dto) {
		Cervejaria cervejaria = repo.findById(id).orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));

		if (cervejariasRepositorio.existsByCervejariaAndIdNot(dto.getCervejaria(), id)) {
			throw new CervejaJaExisteException("Já existe uma Cervejaria com esse nome");
		}

		cervejaria.setCervejaria(dto.getCervejaria());
		cervejaria.setPais(dto.getPais());

		return repo.save(cervejaria); // salva no BD
	}

}
