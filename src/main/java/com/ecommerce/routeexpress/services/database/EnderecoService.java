package com.ecommerce.routeexpress.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.dto.EnderecoDto;
import com.ecommerce.routeexpress.dto.EnderecoResponseDto;
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.models.Endereco;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.EnderecosRepositorio;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service

public class EnderecoService {

	@Autowired
	private EnderecosRepositorio enderecossRepositorio;

	@Autowired
	private ClientesRepositorio clienteRepo;

	public Endereco criaEndereco(EnderecoDto enderecoDto, int clienteId) {

		Cliente cliente = clienteRepo.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

		Endereco endereco = new Endereco();
		endereco.setBairro(enderecoDto.getBairro());
		endereco.setCep(enderecoDto.getCep());
		endereco.setCidade(enderecoDto.getCidade());
		endereco.setComplemento(enderecoDto.getComplemento());
		endereco.setEstado(enderecoDto.getEstado());
		endereco.setLogradouro(enderecoDto.getLogradouro());
		endereco.setLogradouro_numero(enderecoDto.getLogradouro_numero());
		endereco.setTipo_logradouro(enderecoDto.getTipo_logradouro());

		endereco.setCliente(cliente); // <<< MUITO IMPORTANTE

		return enderecossRepositorio.save(endereco);
	}

	public Endereco updateEndereco(int id, EnderecoDto enderecoDto) { // Updates a endereco based on the DTO
		Endereco endereco = enderecossRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Endereco não encontrado"));

		endereco.setBairro(enderecoDto.getBairro());
		endereco.setCep(enderecoDto.getCep());
		endereco.setCidade(enderecoDto.getCidade());
		endereco.setComplemento(enderecoDto.getComplemento());
		endereco.setEstado(enderecoDto.getEstado());
		endereco.setLogradouro(enderecoDto.getLogradouro());
		endereco.setLogradouro_numero(enderecoDto.getLogradouro_numero());
		endereco.setTipo_logradouro(enderecoDto.getTipo_logradouro());

		return enderecossRepositorio.save(endereco);
	}

	public EnderecoDto mapToDto(Endereco endereco) { // Maps a Endereco entity to a EnderecoDto.

		EnderecoDto enderecoDto = new EnderecoDto();
		enderecoDto.setBairro(endereco.getBairro());
		enderecoDto.setCep(endereco.getCep());
		enderecoDto.setCidade(endereco.getCidade());
		enderecoDto.setComplemento(endereco.getComplemento());
		enderecoDto.setEstado(endereco.getEstado());
		enderecoDto.setLogradouro(endereco.getLogradouro());
		enderecoDto.setLogradouro_numero(endereco.getLogradouro_numero());
		enderecoDto.setTipo_logradouro(endereco.getTipo_logradouro());

		return enderecoDto;
	}

	public EnderecoResponseDto mapToResponseDto(Endereco endereco) {
		EnderecoResponseDto dto = new EnderecoResponseDto();
		dto.setId(endereco.getId());
		dto.setBairro(endereco.getBairro());
		dto.setCep(endereco.getCep());
		dto.setCidade(endereco.getCidade());
		dto.setComplemento(endereco.getComplemento());
		dto.setEstado(endereco.getEstado());
		dto.setLogradouro(endereco.getLogradouro());
		dto.setLogradouro_numero(endereco.getLogradouro_numero());
		dto.setTipo_logradouro(endereco.getTipo_logradouro());
		return dto;
	}

	public Endereco findById(int id) { // Find endereco by ID or throw RuntimeException if not found
		return enderecossRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Endereco não encontrada"));
	}

}
