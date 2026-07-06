package com.ecommerce.routeexpress.controllers.cliente;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.routeexpress.dto.EnderecoDto;
import com.ecommerce.routeexpress.dto.EnderecoResponseDto;
import com.ecommerce.routeexpress.models.Endereco;
import com.ecommerce.routeexpress.services.EnderecosRepositorio;
import com.ecommerce.routeexpress.services.database.EnderecoService;

/**
 *
 * @author Daniel Arantes Telles
 */

@RestController
@RequestMapping("/api/enderecos")
//@CrossOrigin(origins = "http://localhost:4200")
public class EnderecoClienteController {

	@Autowired
	private EnderecoService service;

	@Autowired
	private EnderecosRepositorio repo;

	private int getClienteIdLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (int) auth.getPrincipal();
	}

	@GetMapping
	public List<EnderecoResponseDto> listar() {
		return repo.findByClienteId(getClienteIdLogado()).stream().map(service::mapToResponseDto)
				.collect(Collectors.toList());
	}

	@PostMapping
	public EnderecoResponseDto criar(@RequestBody EnderecoDto dto) {
		Endereco salvo = service.criaEndereco(dto, getClienteIdLogado());
		return service.mapToResponseDto(salvo);
	}

	@PutMapping("/{id}")
	public EnderecoResponseDto atualizar(@PathVariable int id, @RequestBody EnderecoDto dto) {
		Endereco atualizado = service.updateEndereco(id, dto);
		return service.mapToResponseDto(atualizado);
	}

}
