package com.ecommerce.routeexpress.controllers;

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

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.CervejaDto;

import com.ecommerce.routeexpress.services.CervejariasRepositorio;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.database.CervejaService;

import jakarta.validation.Valid;

/**
 *
 * @author Daniel A. Telles
 */

@Controller
@RequestMapping("/cervejas")

public class CervejasControle {

	@Autowired
	private CervejasRepositorio repo;

	@Autowired
	private CervejariasRepositorio repo2;

	@Autowired
	private CervejaService cervejaService;

	@GetMapping({ "", "/" })
	public String showCervejaList(Model model) {
		List<Cerveja> cervejas = repo.findAll();
		model.addAttribute("cervejas", cervejas);
		return "cervejas/index"; // diretório cervejas/index
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		CervejaDto cervejaDto = new CervejaDto();
		model.addAttribute("cervejaDto", cervejaDto);
		model.addAttribute("cerveja", new Cerveja());
		model.addAttribute("cervejarias", repo2.findAll());

		return "cervejas/CreateCerveja";
	}

	@PostMapping("/create")
	public String createCerveja(@Valid @ModelAttribute CervejaDto cervejaDto, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) { // Caso algum campo não esteja preenchido, fica na página Createcliente
			return "cervejas/CreateCerveja";
		}

		// Delegate everything to the service
		cervejaService.criaCerveja(cervejaDto);

		return "redirect:/cervejas";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		// Get Cerveja from service
		Cerveja cerveja = cervejaService.findById(id); // lança exceção se não encontrado

		// Add entity to model
		model.addAttribute("cerveja", cerveja);

		// Map entity to DTO
		CervejaDto cervejaDto = cervejaService.mapToDto(cerveja);
		model.addAttribute("cervejaDto", cervejaDto);

		return "cervejas/EditCerveja";
	}

	@PostMapping("/edit")
	public String updateCerveja(@RequestParam int id, @Valid @ModelAttribute CervejaDto cervejaDto,
			BindingResult result) {

		if (result.hasErrors()) {
			return "cervejas/EditCerveja";
		}

		// Delegando toda a lógica de atualização para o service
		cervejaService.atualizaCerveja(id, cervejaDto);

		return "redirect:/cervejas";
	}

	@GetMapping("/delete")
	public String deleteCerveja(@RequestParam int id) {

		// Call the service to handle deletion
		cervejaService.deleteCervejaById(id);

		return "redirect:/cervejas";

	}
}
