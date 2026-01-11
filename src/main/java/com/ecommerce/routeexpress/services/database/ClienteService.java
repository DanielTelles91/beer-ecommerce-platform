package com.ecommerce.routeexpress.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.dto.ClienteDto;
import com.ecommerce.routeexpress.exceptions.CpfJaExisteException;
import com.ecommerce.routeexpress.exceptions.emailJaExisteException;
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.services.ClientesRepositorio;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service

public class ClienteService {

	@Autowired
	private ClientesRepositorio clientesRepositorio;

	public Cliente criaCliente(ClienteDto clienteDto) { // Creates a new Cliente

		if (clientesRepositorio.existsByCpfIgnoreCase(clienteDto.getCpf())) {
			throw new CpfJaExisteException("Já existe esse CPF cadastrado!");
		}

		if (clientesRepositorio.existsByEmailIgnoreCase(clienteDto.getEmail())) {
			throw new emailJaExisteException("Já existe esse email cadastrado!");
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

		return clientesRepositorio.save(cliente);

	}

	public Cliente updateCliente(int id, ClienteDto clienteDto) { // Updates a cliente based on the DTO
		Cliente cliente = clientesRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrada"));

		if (clientesRepositorio.existsByCpfIgnoreCaseAndIdNot(clienteDto.getCpf(), id)) {
			throw new CpfJaExisteException("Já existe esse CPF cadastrado!");
		}

		if (clientesRepositorio.existsByEmailIgnoreCaseAndIdNot(clienteDto.getEmail(), id)) {
			throw new emailJaExisteException("Já existe esse email cadastrado!");
		}

		cliente.setCpf(clienteDto.getCpf());
		cliente.setData_nascimento(clienteDto.getData_nascimento());
		cliente.setEmail(clienteDto.getEmail());
		cliente.setFirst_name(clienteDto.getFirst_name());
		cliente.setLast_name(clienteDto.getLast_name());
		cliente.setSenha(clienteDto.getSenha());
		cliente.setSexo(clienteDto.getSexo());
		cliente.setTelefone(clienteDto.getTelefone());

		return clientesRepositorio.save(cliente);
	}

	public void deleteClienteById(int id) { // Delete cliente
		Cliente cliente = clientesRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrada"));
		clientesRepositorio.delete(cliente);
	}

	public Cliente findById(int id) { // Find cliente by ID or throw RuntimeException if not found
		return clientesRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrada"));
	}

	public ClienteDto mapToDto(Cliente cliente) { // Maps a Cliente entity to a ClienteDto.
		ClienteDto clientedto = new ClienteDto();

		clientedto.setCpf(cliente.getCpf());
		clientedto.setData_nascimento(cliente.getData_nascimento());
		clientedto.setEmail(cliente.getEmail());
		clientedto.setFirst_name(cliente.getFirst_name());
		clientedto.setLast_name(cliente.getLast_name());
		clientedto.setSenha(cliente.getSenha());
		clientedto.setSexo(cliente.getSexo());
		clientedto.setTelefone(cliente.getTelefone());

		return clientedto;
	}

}
