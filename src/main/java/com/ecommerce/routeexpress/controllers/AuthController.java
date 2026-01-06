package com.ecommerce.routeexpress.controllers;

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
* @author Daniel A. Telles
*/

@Controller
public class AuthController {
	
	private final UsuarioRepositorio usuarioRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepositorio usuarioRepo, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Página de troca de senha
    @GetMapping("/login/mudar-senha")
    public String mostrarTrocaSenha(@AuthenticationPrincipal Usuario usuario) {
        if (usuario.isSenhaPadrao()) {
            return "login/mudar_senha";
        }
        return "redirect:/home";
    }

    @PostMapping("/login/mudar-senha")
    public String trocarSenha(@AuthenticationPrincipal Usuario usuario,
                              @RequestParam("novaSenha") String novaSenha) {
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setSenhaPadrao(false);
        usuarioRepo.save(usuario);
        return "redirect:/";
    }
    
 // Página de login
    @GetMapping("/login/telaLogin")
    public String mostrarLogin() {
        return "login/telaLogin"; // caminho do template
    }
}
