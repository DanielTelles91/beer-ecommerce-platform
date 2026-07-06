package com.ecommerce.routeexpress.controllers.adm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.routeexpress.services.database.PedidoAdminService;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
@RequestMapping("/pedidos")
public class PedidosAdmControle {

	@Autowired
	private PedidoAdminService service;

	@GetMapping
	public String listar(@RequestParam(required = false) String status, Model model) {
		model.addAttribute("pedidos", service.listarTodos(status));
		model.addAttribute("statusFiltro", status);
		model.addAttribute("statusOpcoes",
				java.util.List.of("CONFIRMADO", "SEPARANDO_PRODUTOS", "ENVIADO", "ENTREGUE", "CANCELADO"));
		return "pedidos/index";
	}

	@GetMapping("/{id}")
	public String detalhe(@PathVariable Long id, Model model) {
		model.addAttribute("dados", service.buscarDetalhe(id));
		return "pedidos/detalhe";
	}

	@PostMapping("/{id}/status")
	public String atualizarStatus(@PathVariable Long id, @RequestParam String novoStatus) {
		service.atualizarStatus(id, novoStatus);
		return "redirect:/pedidos/" + id;
	}
}
