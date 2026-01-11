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

import com.ecommerce.routeexpress.dto.ClienteDto;
import com.ecommerce.routeexpress.exceptions.CpfJaExisteException;
import com.ecommerce.routeexpress.exceptions.emailJaExisteException;
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.database.ClienteService;

import jakarta.validation.Valid;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
@RequestMapping("/clientes")
public class ClientesControle {

	@Autowired
	private ClientesRepositorio repo;

	@Autowired
	private ClienteService clienteService;

	@GetMapping({ "", "/" })
	public String showClienteList(Model model) {
		List<Cliente> clientes = repo.findAll();
		model.addAttribute("clientes", clientes);
		return "clientes/index"; // diretório clientes/index
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ClienteDto clienteDto = new ClienteDto();
		model.addAttribute("clienteDto", clienteDto);
		return "clientes/CreateCliente";
	}

	@PostMapping("/create")
	public String createCliente(@Valid @ModelAttribute ClienteDto clienteDto, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) { // If any field is not filled in, stay on the CreateClient page
			return "clientes/CreateCliente";

		}

		Cliente cliente;

		try {
			// clienteService.criaCliente(clienteDto);
			cliente = clienteService.criaCliente(clienteDto);
		} catch (CpfJaExisteException e) {
			redirectAttributes.addFlashAttribute("erro", "CPF already exists");
			return "redirect:/clientes/create";
		} catch (emailJaExisteException e) {
			redirectAttributes.addFlashAttribute("erro", "Email already exists");
			return "redirect:/clientes/create";
		}
		// return "redirect:/clientes";
		return "redirect:/enderecos/create?clienteId=" + cliente.getId();

	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		// Get Cliente from service
		Cliente cliente = clienteService.findById(id); // lança exceção se não encontrado

		// Add entity to model
		model.addAttribute("cliente", cliente);

		// Map entity to DTO
		ClienteDto clienteDto = clienteService.mapToDto(cliente);
		model.addAttribute("clienteDto", clienteDto);

		return "clientes/EditCliente";
	}

	@PostMapping("/edit")
	public String updateCliente(Model model, @RequestParam int id, @Valid @ModelAttribute ClienteDto clienteDto,
			BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			result.getAllErrors().forEach(e -> System.out.println(e.getDefaultMessage()));
			Cliente cliente = clienteService.findById(id);
			model.addAttribute("cliente", cliente);
			return "clientes/EditCliente";
		}

		try {
			clienteService.updateCliente(id, clienteDto);
		} catch (CpfJaExisteException e) {
			redirectAttributes.addFlashAttribute("erro", "CPF already exists");
			return "redirect:/clientes/edit?id=" + id;
		} catch (emailJaExisteException e) {
			redirectAttributes.addFlashAttribute("erro", "Email already exists");
			return "redirect:/clientes/edit?id=" + id;
		}

		return "redirect:/clientes";

	}

	@GetMapping("/delete")
	public String deleteCliente(@RequestParam int id) {

		try {

			clienteService.deleteClienteById(id);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/clientes";
		}

		return "redirect:/clientes";
	}

}
