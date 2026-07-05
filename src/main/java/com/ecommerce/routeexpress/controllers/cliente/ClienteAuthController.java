package com.ecommerce.routeexpress.controllers.cliente;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.routeexpress.dto.ClienteDto;
import com.ecommerce.routeexpress.exceptions.emailJaExisteException;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.database.ClienteService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Daniel Arantes Telles
 */

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteAuthController {

	@Autowired
	private ClienteService service;

	@Autowired
	private ClientesRepositorio clientesRepositorio;

	@PostMapping("/definir-senha")
	public String definirSenha(@RequestParam String token, @RequestParam String novaSenha) {
		service.definirSenha(token, novaSenha);
		return "Senha definida com sucesso!";
	}

	@PostMapping("/cadastro")
	public String cadastro(@RequestBody ClienteDto clienteDto) {
		service.cadastroPublico(clienteDto);
		return "Cadastro realizado! Confira seu e-mail para confirmar a conta.";
	}

	@GetMapping("/confirmar-email")
	public String confirmarEmail(@RequestParam String token) {
		service.confirmarEmail(token);
		return "E-mail confirmado com sucesso!";
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
		return service.login(email, senha);

	}

	@GetMapping("/verificar-cpf")
	public Map<String, Boolean> verificarCpf(@RequestParam String cpf) {
		boolean existe = clientesRepositorio.existsByCpfIgnoreCase(cpf);
		return Map.of("disponivel", !existe);
	}

	@GetMapping("/verificar-email")
	public Map<String, Boolean> verificarEmail(@RequestParam String email) {
		boolean existe = clientesRepositorio.existsByEmailIgnoreCase(email);
		return Map.of("disponivel", !existe);
	}

	@GetMapping("/me")
	public ResponseEntity<?> me() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof Integer clienteId)) {
			return ResponseEntity.status(401).body("Não autenticado");
		}
		return ResponseEntity.ok(service.buscarPerfil(clienteId));
	}

	@PutMapping("/me")
	public ResponseEntity<?> editarPerfil(@RequestBody ClienteDto dto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof Integer clienteId)) {
			return ResponseEntity.status(401).body("Não autenticado");
		}
		try {
			return ResponseEntity.ok(service.editarPerfil(clienteId, dto));
		} catch (emailJaExisteException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
