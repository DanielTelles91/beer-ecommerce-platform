package com.ecommerce.routeexpress.controllers.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.routeexpress.dto.PedidoDto;
import com.ecommerce.routeexpress.services.database.CheckoutService;

/**
 *
 * @author Daniel Arantes Telles
 */

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = { "http://localhost:4200", "http://192.168.1.131:4200" })
public class CheckoutController {

	@Autowired
	private CheckoutService service;

	private int getClienteIdLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (int) auth.getPrincipal();
	}

	@PostMapping
	public ResponseEntity<?> finalizar() {
		try {
			PedidoDto pedido = service.finalizarPedido(getClienteIdLogado());
			return ResponseEntity.ok(pedido);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/meus-pedidos")
	public List<PedidoDto> meusPedidos() {
		return service.listarPedidos(getClienteIdLogado());
	}
}
