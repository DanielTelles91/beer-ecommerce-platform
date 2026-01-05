package com.ecommerce.routeexpress.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.models.Cervejaria;
import com.ecommerce.routeexpress.models.CervejariaDto;
import com.ecommerce.routeexpress.services.CervejariasRepositorio;

/**
 *
 * @author Daniel A. Telles
 */

@Service

public class CervejariaService {

	@Autowired
	private CervejariasRepositorio repo;

	// Deleta cervejaria
	public void deleteCervejariaById(int id) {
		Cervejaria cervejaria = repo.findById(id).orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));
		repo.delete(cervejaria);
	}

	// Cria Cervejaria
	public Cervejaria criaCervejaria(CervejariaDto cervejariaDto) {

		Cervejaria cervejaria = new Cervejaria();
		cervejaria.setCervejaria(cervejariaDto.getCervejaria());
		cervejaria.setPais(cervejariaDto.getPais());

		return repo.save(cervejaria); // Salva no BD

		// redirectAttributes.addFlashAttribute("testValue",
		// cervejaria.getCervejaria());
		// return "redirect:/enderecos/create"; // Após persistir o cliente, faz um
		// redirect usando Flash Attributes para transportar o contexto (CPF) de forma
		// segura e temporária evitando exposição de identificadores na URL.
	}

	// Busca cervejaria pelo ID ou lança RuntimeException se não existir
	public Cervejaria findById(int id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));
	}

	// Atualiza cervejaria existente com dados do DTO
	public Cervejaria updateCervejaria(int id, CervejariaDto dto) {
		Cervejaria cervejaria = repo.findById(id).orElseThrow(() -> new RuntimeException("Cervejaria não encontrada"));

		cervejaria.setCervejaria(dto.getCervejaria());
		cervejaria.setPais(dto.getPais());

		return repo.save(cervejaria); // salva no BD
	}

}
