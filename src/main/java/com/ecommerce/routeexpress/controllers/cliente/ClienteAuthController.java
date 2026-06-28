package com.ecommerce.routeexpress.controllers.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.routeexpress.dto.ClienteDto;
import com.ecommerce.routeexpress.services.database.ClienteService;

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
}
