package com.ecommerce.routeexpress.services.database;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.dto.ClienteDto;
import com.ecommerce.routeexpress.exceptions.CpfJaExisteException;
import com.ecommerce.routeexpress.exceptions.emailJaExisteException;
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.security.JwtUtil;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.email.EmailService;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service

public class ClienteService {

	@Autowired
	private ClientesRepositorio clientesRepositorio;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Cliente criaCliente(ClienteDto clienteDto) {

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
		cliente.setSexo(clienteDto.getSexo());
		cliente.setTelefone(clienteDto.getTelefone());

		// Cadastro feito pelo admin: cliente nasce sem senha definida,
		// aguardando confirmação de e-mail + definição de senha pelo próprio cliente.
		cliente.setSenha(null);
		cliente.setEmailConfirmado(false);
		cliente.setTokenConfirmacao(UUID.randomUUID().toString());

		Cliente salvo = clientesRepositorio.save(cliente);

		emailService.enviarEmailDefinirSenha(salvo.getEmail(), salvo.getTokenConfirmacao());

		return salvo;
	}

	public Cliente definirSenha(String token, String novaSenha) {
		Cliente cliente = clientesRepositorio.findByTokenConfirmacao(token)
				.orElseThrow(() -> new RuntimeException("Token inválido ou expirado"));

		cliente.setSenha(passwordEncoder.encode(novaSenha));
		cliente.setEmailConfirmado(true);
		cliente.setTokenConfirmacao(null); // invalida o token, não pode ser reusado

		return clientesRepositorio.save(cliente);
	}

	public Cliente cadastroPublico(ClienteDto clienteDto) {

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
		cliente.setSexo(clienteDto.getSexo());
		cliente.setTelefone(clienteDto.getTelefone());

		// Autocadastro: cliente já define a própria senha
		cliente.setSenha(passwordEncoder.encode(clienteDto.getSenha()));
		cliente.setEmailConfirmado(false);
		cliente.setTokenConfirmacao(UUID.randomUUID().toString());

		Cliente salvo = clientesRepositorio.save(cliente);

		emailService.enviarEmailConfirmacaoCadastro(salvo.getEmail(), salvo.getTokenConfirmacao());

		return salvo;
	}

	public void confirmarEmail(String token) {
		Cliente cliente = clientesRepositorio.findByTokenConfirmacao(token)
				.orElseThrow(() -> new RuntimeException("Token inválido ou expirado"));

		cliente.setEmailConfirmado(true);
		cliente.setTokenConfirmacao(null);

		clientesRepositorio.save(cliente);
	}

	public Cliente updateCliente(int id, ClienteDto clienteDto) {
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
		cliente.setSexo(clienteDto.getSexo());
		cliente.setTelefone(clienteDto.getTelefone());

		// Só atualiza a senha se uma nova senha foi de fato enviada nesse formulário.
		// Edição administrativa comum (telefone, endereço, etc) não deve apagar a senha
		// existente.
		if (clienteDto.getSenha() != null && !clienteDto.getSenha().isBlank()) {
			cliente.setSenha(passwordEncoder.encode(clienteDto.getSenha()));
		}

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
		clientedto.setSexo(cliente.getSexo());
		clientedto.setTelefone(cliente.getTelefone());

		return clientedto;
	}

	@Autowired
	private JwtUtil jwtUtil;

	public ResponseEntity<?> login(String email, String senha) {
		Cliente cliente = clientesRepositorio.findByEmailIgnoreCase(email);

		if (cliente == null || cliente.getSenha() == null || !passwordEncoder.matches(senha, cliente.getSenha())) {
			return ResponseEntity.status(401).body("E-mail ou senha inválidos");
		}

		if (!cliente.isEmailConfirmado()) {
			return ResponseEntity.status(403).body("Confirme seu e-mail antes de fazer login");
		}

		String token = jwtUtil.gerarToken(cliente.getId(), cliente.getEmail());

		return ResponseEntity.ok(Map.of("token", token, "nome", cliente.getFirst_name(), "email", cliente.getEmail()));
	}

	public Map<String, Object> buscarClienteLogado(int clienteId) {
		Cliente cliente = clientesRepositorio.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

		return Map.of("id", cliente.getId(), "nome", cliente.getFirst_name(), "email", cliente.getEmail());
	}

	public ClienteDto buscarPerfil(int clienteId) {
		Cliente cliente = clientesRepositorio.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		return mapToDto(cliente); // já existe no seu service!
	}

	public ClienteDto editarPerfil(int clienteId, ClienteDto dto) {
		Cliente cliente = clientesRepositorio.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

		// valida e-mail se foi alterado
		if (!cliente.getEmail().equalsIgnoreCase(dto.getEmail())) {
			if (clientesRepositorio.existsByEmailIgnoreCaseAndIdNot(dto.getEmail(), clienteId)) {
				throw new emailJaExisteException("Este e-mail já está em uso.");
			}
		}

		cliente.setFirst_name(dto.getFirst_name());
		cliente.setLast_name(dto.getLast_name());
		cliente.setEmail(dto.getEmail());
		cliente.setTelefone(dto.getTelefone());
		cliente.setSexo(dto.getSexo());
		cliente.setData_nascimento(dto.getData_nascimento());
		// CPF e senha não são atualizados aqui

		clientesRepositorio.save(cliente);
		return mapToDto(cliente);
	}

	public void solicitarRecuperacaoSenha(String email) {
		Cliente cliente = clientesRepositorio.findByEmailIgnoreCase(email);
		if (cliente == null)
			return; // não revela se e-mail existe ou não, por questão de segurança

		String token = UUID.randomUUID().toString();
		cliente.setTokenRecuperacaoSenha(token);
		clientesRepositorio.save(cliente);

		emailService.enviarEmailRecuperacaoSenha(cliente.getEmail(), token);
	}

	public void redefinirSenha(String token, String novaSenha) {
		Cliente cliente = clientesRepositorio.findByTokenRecuperacaoSenha(token)
				.orElseThrow(() -> new RuntimeException("Token inválido ou expirado"));

		cliente.setSenha(passwordEncoder.encode(novaSenha));
		cliente.setTokenRecuperacaoSenha(null); // invalida o token !
		clientesRepositorio.save(cliente);
	}

}
