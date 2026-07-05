package com.ecommerce.routeexpress.services.database;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.dto.ListaDeDesejosDto;
import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.models.Estoque;
import com.ecommerce.routeexpress.models.ListaDeDesejos;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.EstoquesRepositorio;
import com.ecommerce.routeexpress.services.ListaDeDesejosRepositorio;

import jakarta.transaction.Transactional;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
public class ListaDeDesejoService {

	@Autowired
	private CervejasRepositorio cervejaRepo;

	@Autowired
	private ClientesRepositorio clienteRepo;

	@Autowired
	private ListaDeDesejosRepositorio listaRepo;

	@Autowired
	private EstoquesRepositorio estoqueRepo;

	public ListaDeDesejoService(ListaDeDesejosRepositorio listaRepo, ClientesRepositorio clienteRepo,
			CervejasRepositorio cervejaRepo) {
		this.listaRepo = listaRepo;
		this.clienteRepo = clienteRepo;
		this.cervejaRepo = cervejaRepo;
	}

	public List<ListaDeDesejos> listarTodos() {
		return listaRepo.findAll();
	}

	@Transactional
	public void adicionar(int clienteId, int cervejaId) {
		Cliente cliente = clienteRepo.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		Cerveja cerveja = cervejaRepo.findById(cervejaId)
				.orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));

		ListaDeDesejos item = new ListaDeDesejos();
		item.setCliente(cliente);
		item.setCerveja(cerveja);

		listaRepo.save(item);
	}

	@Transactional
	public void remover(int clienteId, int cervejaId) {
		listaRepo.deleteByClienteIdAndCervejaId(clienteId, cervejaId);
	}

	public List<ListaDeDesejosDto> listarPorCliente(int clienteId) {
		return listaRepo.findByClienteId(clienteId).stream().map(item -> {
			Cerveja c = item.getCerveja();
			Estoque estoque = estoqueRepo.findFirstByCervejaId(c.getId()).orElse(null);

			ListaDeDesejosDto dto = new ListaDeDesejosDto();
			dto.setCervejaId(c.getId());
			dto.setRotulo(c.getRotulo());
			dto.setCervejaria(c.getCervejaria().getCervejaria());
			dto.setCervejariaId(c.getCervejaria().getId());
			dto.setImagem(c.getImagem_1());
			dto.setPreco(estoque != null ? estoque.getPreco() : 0.0);
			dto.setDataAdicao(item.getDataAdicao());
			dto.setDisponivel(estoque != null && estoque.isDisponibilidade());
			return dto;
		}).collect(Collectors.toList());
	}

	public boolean estaNaLista(int clienteId, int cervejaId) {
		return listaRepo.existsByClienteIdAndCervejaId(clienteId, cervejaId);
	}

}