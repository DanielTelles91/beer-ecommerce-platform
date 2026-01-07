package com.ecommerce.routeexpress.controllers.adm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.database.EstoqueService;

@Controller
@RequestMapping("/estoque")
public class EstoquesControle {

	private final EstoqueService service;

	@Autowired
	private CervejasRepositorio cervejaRepo;

	public EstoquesControle(EstoqueService service) {
		this.service = service;
	}

	// Listar todos os estoques
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("itens", service.listarTodos());
		return "estoque/index";
	}

	// Tela para adicionar novo estoque
	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("cervejas", cervejaRepo.findAll());
		return "estoque/CreateEstoque";
	}

	// Adicionar estoque
	@PostMapping("/adicionar")
	public String adicionar(@RequestParam int cervejaId, @RequestParam int quantidade,
			@RequestParam double porcentagemLucro, @RequestParam int estoqueMinimo, @RequestParam int estoqueMaximo,
			@RequestParam double precoAquisicao, @RequestParam boolean disponibilidade) {
		service.adicionar(cervejaId, quantidade, porcentagemLucro, estoqueMinimo, estoqueMaximo, precoAquisicao,
				disponibilidade);
		return "redirect:/estoque";
	}

	// Remover estoque
	@PostMapping("/remover")
	public String remover(@RequestParam int id) {
		service.remover(id);
		return "redirect:/estoque";
	}
}
