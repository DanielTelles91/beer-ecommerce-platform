package com.ecommerce.routeexpress.controllers.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;

import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.services.CervejasRepositorio;

/**
 *
 * @author Daniel Arantes Telles
 */

@RestController
@RequestMapping("/api/cervejas")
@CrossOrigin(origins = "http://localhost:4200")
public class CervejaClienteController {

	@Autowired
	private CervejasRepositorio repo;

	@GetMapping
	public Page<Cerveja> listar(@RequestParam(required = false) String pais,
			@RequestParam(required = false) String ordenarPreco, // "asc", "desc" ou ausente
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {

		Pageable pageable = PageRequest.of(page, size);

		if ("asc".equalsIgnoreCase(ordenarPreco)) {
			return repo.findDisponiveisPrecoAsc(pais, pageable);
		} else if ("desc".equalsIgnoreCase(ordenarPreco)) {
			return repo.findDisponiveisPrecoDesc(pais, pageable);
		}
		return repo.findDisponiveis(pais, pageable);
	}

	@GetMapping("/buscar")
	public Page<Cerveja> buscarPorNome(@RequestParam String nome, @RequestParam(required = false) String pais,
			@RequestParam(required = false) String ordenarPreco, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "4") int size) {

		Pageable pageable = PageRequest.of(page, size);

		if ("asc".equalsIgnoreCase(ordenarPreco)) {
			return repo.buscarPorNomePrecoAsc(nome, pais, pageable);
		} else if ("desc".equalsIgnoreCase(ordenarPreco)) {
			return repo.buscarPorNomePrecoDesc(nome, pais, pageable);
		}
		return repo.buscarPorNome(nome, pais, pageable);
	}

	@GetMapping("/paises")
	public List<String> listarPaises() {
		return repo.listarPaisesDisponiveis();
	}

	@GetMapping("/{id}")
	public Cerveja buscarPorId(@PathVariable int id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));
	}
}
