package com.ecommerce.routeexpress.controllers.cliente;

/**
*
* @author Daniel Arantes Telles
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ecommerce.routeexpress.dto.CarrinhoDto;
import com.ecommerce.routeexpress.services.database.CarrinhoService;

@RestController
@RequestMapping("/api/carrinho")
@CrossOrigin(origins = "http://localhost:4200")

public class CarrinhoController {

	@Autowired
	private CarrinhoService service;

	@GetMapping
	public CarrinhoDto buscar(@RequestParam String sessionId) {
		return service.buscarCarrinho(sessionId);
	}

	@PostMapping("/itens")
	public CarrinhoDto adicionar(@RequestParam String sessionId, @RequestParam int cervejaId,
			@RequestParam(defaultValue = "1") int quantidade) {
		return service.adicionarItem(sessionId, cervejaId, quantidade);
	}

	@PutMapping("/itens/{itemId}")
	public CarrinhoDto atualizar(@RequestParam String sessionId, @PathVariable Long itemId,
			@RequestParam int quantidade) {
		return service.atualizarQuantidade(sessionId, itemId, quantidade);
	}

	@DeleteMapping("/itens/{itemId}")
	public CarrinhoDto remover(@RequestParam String sessionId, @PathVariable Long itemId) {
		return service.removerItem(sessionId, itemId);
	}

	@PostMapping("/merge")
	public ResponseEntity<?> merge(@RequestParam String sessionId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !(auth.getPrincipal() instanceof Integer clienteId)) {
			return ResponseEntity.status(401).body("Não autenticado");
		}

		return ResponseEntity.ok(service.mergeCarrinho(sessionId, clienteId));
	}
}
