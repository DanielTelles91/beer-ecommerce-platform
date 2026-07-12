package com.ecommerce.routeexpress.controllers.adm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.routeexpress.services.PedidoStatusHistoricoRepositorio;
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
	
	@Autowired
	private PedidoStatusHistoricoRepositorio historicoRepo;

	@GetMapping
	public String listar(
	        @RequestParam(required = false) String status,
	        @RequestParam(required = false) String busca,
	        @RequestParam(required = false) String dataInicio,
	        @RequestParam(required = false) String dataFim,
	        Model model) {

	    model.addAttribute("pedidos", service.listarTodos(status, busca, dataInicio, dataFim));
	    model.addAttribute("contadores", service.contadoresPorStatus());
	    model.addAttribute("statusFiltro", status);
	    model.addAttribute("busca", busca);
	    model.addAttribute("dataInicio", dataInicio);
	    model.addAttribute("dataFim", dataFim);
	    model.addAttribute("statusOpcoes",
	        List.of("CONFIRMADO", "SEPARANDO_PRODUTOS", "ENVIADO", "ENTREGUE", "CANCELADO"));
	    return "pedidos/index";
	}

	@GetMapping("/{id}")
	public String detalhe(@PathVariable Long id, Model model) {
		model.addAttribute("dados", service.buscarDetalhe(id));
		model.addAttribute("historico", historicoRepo.findByPedidoIdOrderByDataMudancaAsc(id));
		return "pedidos/detalhe";
	}

	@PostMapping("/{id}/status")
	public String atualizarStatus(@PathVariable Long id, @RequestParam String novoStatus) {
		service.atualizarStatus(id, novoStatus);
		return "redirect:/pedidos/" + id;
	}
}
