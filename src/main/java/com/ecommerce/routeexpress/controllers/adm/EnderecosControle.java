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
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.models.Endereco;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.EnderecosRepositorio;

import jakarta.validation.Valid;

/**
 *
 * @author Daniel A. Telles
 */

@Controller
@RequestMapping("/enderecos")
public class EnderecosControle {

	@Autowired
	private EnderecosRepositorio repo;

	@GetMapping({ "", "/" })
	public String showEnderecoList(Model model) {
		List<Endereco> enderecos = repo.findAll();
		model.addAttribute("enderecos", enderecos);
		return "enderecos/index"; // diretório enderecos/index
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		EnderecoDto enderecoDto = new EnderecoDto();
		model.addAttribute("enderecoDto", enderecoDto);

		String value = (String) model.getAttribute("testValue"); // Pega o cont. CPF e seta o valor na variável.
		System.out.println("Flash Value = " + value);
		enderecoDto.setCpf(value);

		return "enderecos/CreateEndereco";
	}

	@Autowired
	private ClientesRepositorio clienteRepo;

	@PostMapping("/create")
	public String createEndereco(@Valid @ModelAttribute EnderecoDto enderecoDto, BindingResult result) {

		if (result.hasErrors()) { // Caso algum campo não esteja preenchido, fica na página CreateEndereco
			return "enderecos/CreateEndereco";
		}

		// Busca cliente pelo CPF
		Cliente cliente = clienteRepo.findByCpf(enderecoDto.getCpf());

		if (cliente == null) {
			// aqui você pode lançar exceção ou redirecionar com erro
			throw new RuntimeException("Cliente não encontrado!");
		}

		Endereco endereco = new Endereco();
		endereco.setBairro(enderecoDto.getBairro());
		endereco.setCep(enderecoDto.getCep());
		endereco.setCidade(enderecoDto.getCidade());
		endereco.setComplemento(enderecoDto.getComplemento());
		endereco.setCpf(enderecoDto.getCpf());
		endereco.setEstado(enderecoDto.getEstado());
		endereco.setLogradouro(enderecoDto.getLogradouro());
		endereco.setLogradouro_numero(enderecoDto.getLogradouro_numero());
		endereco.setTipo_logradouro(enderecoDto.getTipo_logradouro());

		endereco.setCliente(cliente); // <<< MUITO IMPORTANTE

		repo.save(endereco); // Salva no BD

		return "redirect:/enderecos";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		try {
			Endereco endereco = repo.findById(id).get();
			model.addAttribute("endereco", endereco);

			EnderecoDto enderecoDto = new EnderecoDto();
			enderecoDto.setBairro(endereco.getBairro());
			enderecoDto.setCep(endereco.getCep());
			enderecoDto.setCidade(endereco.getCidade());
			enderecoDto.setComplemento(endereco.getComplemento());
			enderecoDto.setCpf(endereco.getCpf());
			enderecoDto.setEstado(endereco.getEstado());
			enderecoDto.setLogradouro(endereco.getLogradouro());
			enderecoDto.setLogradouro_numero(endereco.getLogradouro_numero());
			enderecoDto.setTipo_logradouro(endereco.getTipo_logradouro());

			model.addAttribute("enderecoDto", enderecoDto);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/products";
		}

		return "enderecos/EditEndereco";
	}

	@PostMapping("/edit")
	public String updateEndereco(Model model, @RequestParam int id, @Valid @ModelAttribute EnderecoDto enderecoDto,
			BindingResult result) {

		try {
			Endereco endereco = repo.findById(id).get();
			model.addAttribute("endereco", endereco);

			if (result.hasErrors()) {
				return "enderecos/EditEndereco";
			}
			endereco.setBairro(enderecoDto.getBairro());
			endereco.setCep(enderecoDto.getCep());
			endereco.setCidade(enderecoDto.getCidade());
			endereco.setComplemento(enderecoDto.getComplemento());
			endereco.setCpf(enderecoDto.getCpf());
			endereco.setEstado(enderecoDto.getEstado());
			endereco.setLogradouro(enderecoDto.getLogradouro());
			endereco.setLogradouro_numero(enderecoDto.getLogradouro_numero());
			endereco.setTipo_logradouro(enderecoDto.getTipo_logradouro());

			repo.save(endereco); // Salva no banco de dados
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/products";
		}

		return "redirect:/enderecos";

	}

	@GetMapping("/delete")
	public String deleteEndereco(@RequestParam int id) {

		try {
			Endereco endereco = repo.findById(id).get();

			repo.delete(endereco);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/products";
		}

		return "redirect:/enderecos";
	}
}
