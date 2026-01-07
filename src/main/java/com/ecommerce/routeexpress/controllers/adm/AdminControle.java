package com.ecommerce.routeexpress.controllers.adm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
public class AdminControle {

	@GetMapping("/telaInicialAdm")
	public String telaInicialAdm() {
		return "adm/telaInicialAdm";
	}

	@GetMapping
	public String index() {
	    return "redirect:/adm/telaInicialAdm";
	}
}
