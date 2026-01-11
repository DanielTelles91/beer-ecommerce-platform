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

import com.ecommerce.routeexpress.dto.EnderecoDto;
import com.ecommerce.routeexpress.models.Endereco;
import com.ecommerce.routeexpress.services.EnderecosRepositorio;
import com.ecommerce.routeexpress.services.database.EnderecoService;

import jakarta.validation.Valid;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
@RequestMapping("/enderecos")
public class EnderecosControle {

	@Autowired
	private EnderecosRepositorio repo;

	@Autowired
	private EnderecoService enderecoService;

	@GetMapping
	public String showEnderecoList(@RequestParam(required = false) Integer clienteId, Model model) {

		List<Endereco> enderecos;

		if (clienteId != null) {
			enderecos = repo.findByClienteId(clienteId);
			model.addAttribute("clienteId", clienteId);
		} else {
			enderecos = repo.findAll();
		}

		model.addAttribute("enderecos", enderecos);
		return "enderecos/index";
	}

	@GetMapping("/create")
	public String showCreatePage(@RequestParam int clienteId, Model model) {
		EnderecoDto enderecoDto = new EnderecoDto();
		model.addAttribute("enderecoDto", enderecoDto);
		model.addAttribute("clienteId", clienteId);
		return "enderecos/CreateEndereco";
	}

	@PostMapping("/create")
	public String createEndereco(@RequestParam int clienteId, @Valid @ModelAttribute EnderecoDto enderecoDto,
			BindingResult result, Model model) {

		if (result.hasErrors()) { // Caso algum campo não esteja preenchido, fica na página CreateEndereco
			model.addAttribute("clienteId", clienteId); // pra não perder no reload
			return "enderecos/CreateEndereco";
		}

		enderecoService.criaEndereco(enderecoDto, clienteId);

		return "redirect:/enderecos";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		Endereco endereco = enderecoService.findById(id);

		// Add entity to model
		model.addAttribute("endereco", endereco);

		// Map entity to DTO
		EnderecoDto enderecoDto = enderecoService.mapToDto(endereco);
		model.addAttribute("enderecoDto", enderecoDto);

		return "enderecos/EditEndereco";
	}

	@PostMapping("/edit")
	public String updateEndereco(Model model, @RequestParam int id, @Valid @ModelAttribute EnderecoDto enderecoDto,
			BindingResult result) {

		if (result.hasErrors()) {
			result.getAllErrors().forEach(e -> System.out.println(e.getDefaultMessage()));
			Endereco endereco = enderecoService.findById(id);
			model.addAttribute("endereco", endereco);
			return "enderecos/EditEndereco";
		}

		enderecoService.updateEndereco(id, enderecoDto);

		return "redirect:/enderecos";

	}

	@GetMapping("/delete")
	public String deleteEndereco(@RequestParam int id) {

		return "redirect:/enderecos";
	}
}
