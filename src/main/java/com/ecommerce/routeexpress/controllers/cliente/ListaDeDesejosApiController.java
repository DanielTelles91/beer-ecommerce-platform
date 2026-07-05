package com.ecommerce.routeexpress.controllers.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.routeexpress.dto.ListaDeDesejosDto;
import com.ecommerce.routeexpress.services.database.ListaDeDesejoService;

/**
 *
 * @author Daniel Arantes Telles
 */

@RestController
@RequestMapping("/api/lista-desejos")
@CrossOrigin(origins = { "http://localhost:4200", "http://192.168.1.131:4200" })
public class ListaDeDesejosApiController {

	@Autowired
	private ListaDeDesejoService service;

	private int getClienteIdLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (int) auth.getPrincipal();
	}

	@GetMapping
	public List<ListaDeDesejosDto> listar() {
		return service.listarPorCliente(getClienteIdLogado());
	}

	@GetMapping("/verifica/{cervejaId}")
	public ResponseEntity<Boolean> verifica(@PathVariable int cervejaId) {
		return ResponseEntity.ok(service.estaNaLista(getClienteIdLogado(), cervejaId));
	}

	@PostMapping("/{cervejaId}")
	public ResponseEntity<String> adicionar(@PathVariable int cervejaId) {
		service.adicionar(getClienteIdLogado(), cervejaId);
		return ResponseEntity.ok("Adicionado");
	}

	@DeleteMapping("/{cervejaId}")
	public ResponseEntity<String> remover(@PathVariable int cervejaId) {
		service.remover(getClienteIdLogado(), cervejaId);
		return ResponseEntity.ok("Removido");
	}
}
