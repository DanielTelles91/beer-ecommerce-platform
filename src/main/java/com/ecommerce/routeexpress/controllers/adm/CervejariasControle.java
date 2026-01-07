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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.routeexpress.dto.CervejariaDto;
import com.ecommerce.routeexpress.models.Cervejaria;
import com.ecommerce.routeexpress.services.CervejariasRepositorio;
import com.ecommerce.routeexpress.services.database.CervejariaService;
import com.ecommerce.routeexpress.services.storage.ImageStorageService;

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

	@Autowired
	private ImageStorageService imageStorageService;

	@Autowired
	private CervejariaService cervejariaService;

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

		cervejariaService.criaCervejaria(cervejariaDto); // call the service

		return "redirect:/cervejarias";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		// chama o service
		Cervejaria cervejaria = cervejariaService.findById(id);

		// adiciona objeto principal
		model.addAttribute("cervejaria", cervejaria);

		// prepara DTO para o formulário
		CervejariaDto cervejariaDto = new CervejariaDto();
		cervejariaDto.setCervejaria(cervejaria.getCervejaria());
		cervejariaDto.setPais(cervejaria.getPais());
		model.addAttribute("cervejariaDto", cervejariaDto);

		return "cervejarias/EditCervejaria";
	}

	@PostMapping("/edit")
	public String updateCervejaria(Model model, @RequestParam int id,
			@Valid @ModelAttribute CervejariaDto cervejariaDto, BindingResult result) {

		if (result.hasErrors()) {
			// mantém os campos preenchidos no form
			Cervejaria cervejaria = cervejariaService.findById(id);
			model.addAttribute("cervejaria", cervejaria);
			return "cervejarias/EditCervejaria";
		}

		// chama o service para atualizar
		cervejariaService.updateCervejaria(id, cervejariaDto);

		return "redirect:/cervejarias";

	}

	@GetMapping("/delete")
	public String deleteCervejaria(@RequestParam int id) {

		imageStorageService.apagaPastaImagem(id); // Delete the image folder for this brewery

		cervejariaService.deleteCervejariaById(id); // Delete the brewery from the database via service

		return "redirect:/cervejarias"; // Redirect to the list page
	}

}
