package com.ecommerce.routeexpress.controllers.adm;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.routeexpress.models.Usuario;
import com.ecommerce.routeexpress.services.UsuarioRepositorio;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
public class AdminAuthControle {

	private final UsuarioRepositorio usuarioRepo;
	private final BCryptPasswordEncoder passwordEncoder;

	public AdminAuthControle(UsuarioRepositorio usuarioRepo, BCryptPasswordEncoder passwordEncoder) {
		this.usuarioRepo = usuarioRepo;
		this.passwordEncoder = passwordEncoder;
	}

	// Página de troca de senha
	@GetMapping("/adm/mudar-senha")
	public String mostrarTrocaSenha() {
		return "adm/mudar_senha";
	}

	@PostMapping("/adm/mudar-senha")
	public String trocarSenha(@AuthenticationPrincipal Usuario usuario, @RequestParam String novaSenha,
			@RequestParam String senhaAtual) {

		// valida senha atual antes de trocar
		if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
			return "redirect:/adm/mudar-senha?erro=true";
		}

		usuario.setSenha(passwordEncoder.encode(novaSenha));
		usuario.setSenhaPadrao(false);
		usuarioRepo.save(usuario);
		return "redirect:/adm/telaInicialAdm?senhaTrocada=true";
	}

	// Página de login
	@GetMapping("/adm/telaLogin")
	public String mostrarLogin() {
		return "adm/telaLogin"; // caminho do template
	}
}
