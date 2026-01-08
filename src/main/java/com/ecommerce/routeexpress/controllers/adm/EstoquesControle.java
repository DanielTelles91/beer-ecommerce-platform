package com.ecommerce.routeexpress.controllers.adm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.ecommerce.routeexpress.dto.EstoqueDto;
import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.Estoque;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.EstoquesRepositorio;
import com.ecommerce.routeexpress.services.database.EstoqueService;

import jakarta.validation.Valid;

/**
*
* @author Daniel Arantes Telles
*/

@Controller
@RequestMapping("/estoque")
public class EstoquesControle {

	private final EstoqueService service;

	@Autowired
	private CervejasRepositorio cervejaRepo;
	
	@Autowired
	private EstoquesRepositorio estoquesRepositorio;
	

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
		// Todas as cervejas
	    List<Cerveja> todas = cervejaRepo.findAll();

	    // IDs das cervejas que já estão no estoque
	    List<Integer> idsEmEstoque = estoquesRepositorio.findAllCervejaIdsEmEstoque();

	    // Filtra: só as que NÃO estão no estoque
	    List<Cerveja> disponiveis = todas.stream()
	        .filter(c -> !idsEmEstoque.contains(c.getId()))
	        .toList();

	    model.addAttribute("cervejas", disponiveis);
		return "estoque/CreateEstoque";
	}

	// Adicionar estoque
	@PostMapping("/adicionar")
	public String adicionar(@RequestParam int cervejaId, @RequestParam int quantidade,
			@RequestParam double porcentagemLucro, @RequestParam int estoqueMinimo, @RequestParam int estoqueMaximo,
			@RequestParam double precoAquisicao, @RequestParam boolean disponibilidade) {
		service.adicionarEstoque(cervejaId, quantidade, porcentagemLucro, estoqueMinimo, estoqueMaximo, precoAquisicao,
				disponibilidade);
		return "redirect:/estoque";
	}


	
	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		// Get Estoque from service
		Estoque estoque = service.findById(id); // lança exceção se não encontrado

		// Add entity to model
		model.addAttribute("estoque", estoque);

		// Map entity to DTO
		EstoqueDto estoqueDto = service.mapToDto(estoque);
		model.addAttribute("estoqueDto", estoqueDto);

		return "estoque/EditEstoque";
	}
	
	
	
	
	@PostMapping("/edit")
	public String updateEstoque(@RequestParam int id, @Valid @ModelAttribute EstoqueDto estoqueDto,
			BindingResult result)  {

		if (result.hasErrors()) {
			return "estoque/EditEstoque";
		}

				
		service.atualizaEstoque(id, estoqueDto);
		

		return "redirect:/estoque";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@GetMapping("/delete")
	public String deleteEstoque(@RequestParam int id) {

		service.removerEstoque(id);

		return "redirect:/estoque";
	}
}
