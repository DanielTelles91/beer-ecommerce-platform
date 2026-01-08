package com.ecommerce.routeexpress.controllers.adm;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.routeexpress.models.Usuario;

/**
*
* @author Daniel Arantes Telles
*/

@Controller
@RequestMapping("/adm")
public class AdminControle {

	@GetMapping("/telaInicialAdm")
	public String telaInicialAdm(@AuthenticationPrincipal Usuario usuario, Model model) {
	
		if (usuario == null) {
	        System.out.println("USUÁRIO NULL");
	    } else {
	        System.out.println("Usuário logado: " + usuario.getUsername());
	    }

	    model.addAttribute("usuario", usuario);
		
		return "adm/telaInicialAdm";
	}

	@GetMapping
	public String index() {
	    return "redirect:/adm/telaInicialAdm";
	}
}
