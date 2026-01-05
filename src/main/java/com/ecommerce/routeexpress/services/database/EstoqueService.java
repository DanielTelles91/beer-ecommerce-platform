package com.ecommerce.routeexpress.services.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.Estoque;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.EstoquesRepositorio;

import jakarta.transaction.Transactional;

@Service
public class EstoqueService {

	@Autowired
	private EstoquesRepositorio estoqueRepo;

	@Autowired
	private CervejasRepositorio cervejaRepo;

	public List<Estoque> listarTodos() {
		return estoqueRepo.findAll();
	}

	@Transactional
	public void adicionar(int cervejaId, int quantidade, double porcentagemLucro, int estoqueMinimo, int estoqueMaximo,
			double precoAquisicao, boolean disponibilidade) {
		Cerveja cerveja = cervejaRepo.findById(cervejaId)
				.orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));

		Estoque estoque = new Estoque();
		estoque.setCerveja(cerveja);
		estoque.setQuantidade(quantidade);
		estoque.setPorcentagemLucro(porcentagemLucro);
		estoque.setEstoqueMinimo(estoqueMinimo);
		estoque.setEstoqueMaximo(estoqueMaximo);
		estoque.setPrecoAquisicao(precoAquisicao);
		estoque.setDisponibilidade(disponibilidade);
		estoque.calcularLucro();

		estoqueRepo.save(estoque);
	}

	@Transactional
	public void remover(int id) {
		estoqueRepo.deleteById(id);
	}

}
