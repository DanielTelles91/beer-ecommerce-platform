package com.ecommerce.routeexpress.services.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.models.ListaDeDesejos;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
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

	// Construtor (opcional, se usar @Autowired nos campos)
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
}