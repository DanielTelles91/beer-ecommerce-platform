package com.ecommerce.routeexpress.controllers.adm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.routeexpress.models.Usuario;
import com.ecommerce.routeexpress.services.dashboard.DashboardService;
import com.ecommerce.routeexpress.services.database.PedidoAdminService;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
@RequestMapping("/adm")
public class AdminControle {

	private final DashboardService dashboardService;

	@Autowired
	private PedidoAdminService pedidoAdminService;

	public AdminControle(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/telaInicialAdm")
	public String telaInicialAdm(@AuthenticationPrincipal Usuario usuario, @RequestParam(defaultValue = "0") int ano,
			Model model) {

		if (ano == 0)
			ano = LocalDate.now().getYear();

		model.addAttribute("usuario", usuario);
		model.addAttribute("angularOnline", dashboardService.checkAngular());
		model.addAttribute("mysqlOnline", dashboardService.checkMysql());

		Map<String, Object> dashboard = pedidoAdminService.dadosDashboard(ano);
		model.addAttribute("dashboard", dashboard);
		model.addAttribute("anoSelecionado", ano);
		model.addAttribute("anos", gerarListaAnos());

		return "adm/telaInicialAdm";
	}

	@GetMapping
	public String index() {
		return "redirect:/adm/telaInicialAdm";
	}

	private List<Integer> gerarListaAnos() {
		int anoAtual = LocalDate.now().getYear();
		List<Integer> anos = new ArrayList<>();
		for (int a = anoAtual; a >= anoAtual - 4; a--)
			anos.add(a);
		return anos;
	}
}