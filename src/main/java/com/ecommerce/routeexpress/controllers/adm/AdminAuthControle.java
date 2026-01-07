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
* @author Daniel A. Telles
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
    public String mostrarTrocaSenha(@AuthenticationPrincipal Usuario usuario) {
        if (usuario.isSenhaPadrao()) {
            return "adm/mudar_senha";
        }
        return "redirect:/home";
    }

    @PostMapping("/adm/mudar-senha")
    public String trocarSenha(@AuthenticationPrincipal Usuario usuario,
                              @RequestParam("novaSenha") String novaSenha) {
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setSenhaPadrao(false);
        usuarioRepo.save(usuario);
        return "redirect:/adm/telaInicialAdm";
    }
    
 // Página de login
    @GetMapping("/adm/telaLogin")
    public String mostrarLogin() {
        return "adm/telaLogin"; // caminho do template
    }
}
