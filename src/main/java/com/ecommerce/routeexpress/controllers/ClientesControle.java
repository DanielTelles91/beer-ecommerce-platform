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

import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.models.ClienteDto;
import com.ecommerce.routeexpress.services.ClientesRepositorio;

import jakarta.validation.Valid;


/**
*
* @author Daniel A. Telles
*/
 
@Controller
@RequestMapping("/clientes")
public class ClientesControle {

	
	@Autowired
	private ClientesRepositorio repo;
	

	@GetMapping({"", "/"})
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
	public String createCliente(
		@Valid @ModelAttribute ClienteDto clienteDto,
		BindingResult result, RedirectAttributes redirectAttributes
		) {
		
		if (result.hasErrors()) { // Caso algum campo não esteja preenchido, fica na página Createcliente
			return "clientes/CreateCliente";
			 
		}	
		
		Cliente cliente = new Cliente();
		cliente.setCpf(clienteDto.getCpf());
		cliente.setData_nascimento(clienteDto.getData_nascimento());
		cliente.setEmail(clienteDto.getEmail());
		cliente.setFirst_name(clienteDto.getFirst_name());
		cliente.setLast_name(clienteDto.getLast_name());
		cliente.setSenha(clienteDto.getSenha());
		cliente.setSexo(clienteDto.getSexo());
		cliente.setTelefone(clienteDto.getTelefone());
		
		repo.save(cliente);  //Salva no BD
		
		redirectAttributes.addFlashAttribute("testValue", cliente.getCpf()); 
        return "redirect:/enderecos/create";  // Após persistir o cliente, faz um redirect usando Flash Attributes para transportar o contexto (CPF) de forma
			                                  // segura e temporária evitando exposição de identificadores na URL.
	}
	

	@GetMapping("/edit")
	public String showEditPage(
			Model model,
			@RequestParam int id
			) {
		
		try {
			Cliente cliente = repo.findById(id).get();
			model.addAttribute("cliente", cliente);
			
			ClienteDto clienteDto = new ClienteDto();
			clienteDto.setCpf(cliente.getCpf());
			clienteDto.setData_nascimento(cliente.getData_nascimento());
			clienteDto.setEmail(cliente.getEmail());
			clienteDto.setFirst_name(cliente.getFirst_name());
			clienteDto.setLast_name(cliente.getLast_name());
			clienteDto.setSenha(cliente.getSenha());
			clienteDto.setSexo(cliente.getSexo());
			clienteDto.setTelefone(cliente.getTelefone());
			
			model.addAttribute("clienteDto", clienteDto);
			
		}
		catch(Exception ex) {
			System.out.println("Exception: "+ ex.getMessage());
			return "redirect:/products";
		}	
		
		return "clientes/EditCliente";
	}
	
	@PostMapping("/edit")
	public String updateCliente(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute ClienteDto clienteDto,
			BindingResult result
			) {
		
		try {
			Cliente cliente = repo.findById(id).get();
			model.addAttribute("cliente", cliente);
			
			if (result.hasErrors()) {
				return "clientes/EditCliente";
			}
			cliente.setCpf(clienteDto.getCpf());
			cliente.setData_nascimento(clienteDto.getData_nascimento());
			cliente.setEmail(clienteDto.getEmail());
			cliente.setFirst_name(clienteDto.getFirst_name());
			cliente.setLast_name(clienteDto.getLast_name());
			cliente.setSenha(clienteDto.getSenha());
			cliente.setSexo(clienteDto.getSexo());
			cliente.setTelefone(clienteDto.getTelefone());
			
			repo.save(cliente); // Salva no banco de dados
		}
		catch(Exception ex) {
			System.out.println("Exception: "+ ex.getMessage());
			return "redirect:/products";
		}	
		
		return "redirect:/clientes";
		
	}
	
	
	@GetMapping("/delete")
	public String deleteCliente(
			@RequestParam int id
			) {
		
		try {
			Cliente cliente = repo.findById(id).get();
			
			repo.delete(cliente);
			
		}
		catch(Exception ex) {
			System.out.println("Exception: "+ ex.getMessage());
			return "redirect:/products";
		}
		
		return "redirect:/clientes";
	}
	
}
