package com.ecommerce.routeexpress.controllers.adm;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ecommerce.routeexpress.models.Usuario;
import com.ecommerce.routeexpress.services.dashboard.DashboardService;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
@RequestMapping("/adm")
public class AdminControle {

	private final DashboardService dashboardService;

	public AdminControle(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/telaInicialAdm")
	public String telaInicialAdm(@AuthenticationPrincipal Usuario usuario, Model model) {

		if (usuario == null) {
			System.out.println("USUÁRIO NULL");
		} else {
			System.out.println("Usuário logado: " + usuario.getUsername());
		}

		model.addAttribute("usuario", usuario);

		model.addAttribute("angularOnline", dashboardService.checkAngular()); // add atributo se angular está online

		model.addAttribute("mysqlOnline", dashboardService.checkMysql()); // add atributo se mysql está online

		return "adm/telaInicialAdm";
	}

	@GetMapping
	public String index() {
		return "redirect:/adm/telaInicialAdm";
	}
}
