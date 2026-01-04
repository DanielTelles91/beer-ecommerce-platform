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

import com.ecommerce.routeexpress.models.Cervejaria;
import com.ecommerce.routeexpress.models.CervejariaDto;
import com.ecommerce.routeexpress.services.CervejariasRepositorio;

import jakarta.validation.Valid;

/**
 *
 * @author Daniel A. Telles
 */

@Controller
@RequestMapping("/cervejarias")
public class CervejariasControle {

	@Autowired
	private CervejariasRepositorio repo;

	@GetMapping({ "", "/" })
	public String showCervejariaList(Model model) {
		List<Cervejaria> cervejarias = repo.findAll();
		model.addAttribute("cervejarias", cervejarias);
		return "cervejarias/index"; // diretório cervejarias/index
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		CervejariaDto cervejariaDto = new CervejariaDto();
		model.addAttribute("cervejariaDto", cervejariaDto);
		return "cervejarias/CreateCervejaria";
	}

	@PostMapping("/create")
	public String createCervejaria(@Valid @ModelAttribute CervejariaDto cervejariaDto, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) { // Caso algum campo não esteja preenchido, fica na página Createcliente
			return "cervejarias/CreateCervejaria";

		}

		Cervejaria cervejaria = new Cervejaria();
		cervejaria.setCervejaria(cervejariaDto.getCervejaria());
		cervejaria.setPais(cervejariaDto.getPais());

		repo.save(cervejaria); // Salva no BD

		// redirectAttributes.addFlashAttribute("testValue",
		// cervejaria.getCervejaria());
		// return "redirect:/enderecos/create"; // Após persistir o cliente, faz um
		// redirect usando Flash Attributes para transportar o contexto (CPF) de forma
		// segura e temporária evitando exposição de identificadores na URL.
		return "redirect:/cervejarias";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		try {
			Cervejaria cervejaria = repo.findById(id).get();
			model.addAttribute("cervejaria", cervejaria);

			CervejariaDto cervejariaDto = new CervejariaDto();
			cervejariaDto.setCervejaria(cervejaria.getCervejaria());
			cervejariaDto.setPais(cervejaria.getPais());

			model.addAttribute("cervejariaDto", cervejariaDto);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/cervejarias";
		}

		return "cervejarias/EditCervejaria";
	}

	@PostMapping("/edit")
	public String updateCervejaria(Model model, @RequestParam int id,
			@Valid @ModelAttribute CervejariaDto cervejariaDto, BindingResult result) {

		try {
			Cervejaria cervejaria = repo.findById(id).get();
			model.addAttribute("cervejaria", cervejaria);

			if (result.hasErrors()) {
				return "cervejarias/EditCervejaria";
			}
			cervejaria.setCervejaria(cervejariaDto.getCervejaria());
			cervejaria.setPais(cervejariaDto.getPais());

			repo.save(cervejaria); // Salva no banco de dados
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/cervejarias";
		}

		return "redirect:/cervejarias";

	}

	@GetMapping("/delete")
	public String deleteCervejaria(@RequestParam int id) {

		try {
			Cervejaria cervejaria = repo.findById(id).get();

			repo.delete(cervejaria);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/cervejarias";
		}

		return "redirect:/cervejarias";
	}

}
