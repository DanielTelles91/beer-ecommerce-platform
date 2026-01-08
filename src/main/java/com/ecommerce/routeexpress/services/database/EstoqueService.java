package com.ecommerce.routeexpress.services.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.dto.EstoqueDto;
import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.Estoque;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.EstoquesRepositorio;

import jakarta.transaction.Transactional;

/**
 *
 * @author Daniel Arantes Telles
 */

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
	public void adicionarEstoque(int cervejaId, int quantidade, double porcentagemLucro, int estoqueMinimo,
			int estoqueMaximo, double precoAquisicao, boolean disponibilidade) {
		Cerveja cerveja = cervejaRepo.findById(cervejaId)
				.orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));

		if (estoqueRepo.existsByCervejaId(cervejaId)) {
			throw new RuntimeException("Essa cerveja já possui estoque cadastrado.");
		}

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

	/**
	 * Atualiza um estoque com base no DTO
	 */
	public void atualizaEstoque(int id, EstoqueDto dto) {

		Estoque estoque = estoqueRepo.findById(id).orElseThrow(() -> new RuntimeException("Estoque não encontrada"));

		// int estoqueIdf = estoque.getCervejaria().getId();
		// int estoqueId = estoque.getId();

		estoque.setEstoqueMaximo(dto.getEstoqueMaximo());
		estoque.setQuantidade(dto.getQuantidade());
		estoque.setPorcentagemLucro(dto.getPorcentagemLucro());
		estoque.setEstoqueMinimo(dto.getEstoqueMinimo());
		estoque.setEstoqueMaximo(dto.getEstoqueMaximo());
		estoque.setPrecoAquisicao(dto.getPrecoAquisicao());
		estoque.setDisponibilidade(dto.isDisponibilidade());
		estoque.calcularLucro();
		estoqueRepo.save(estoque);
	}

	/**
	 * Finds a Cerveja by ID. Throws RuntimeException if not found.
	 */
	public Estoque findById(int id) {
		return estoqueRepo.findById(id).orElseThrow(() -> new RuntimeException("Estoque not found"));
	}

	/**
	 * Maps a Estoque entity to a EstoqueDto.
	 */
	public EstoqueDto mapToDto(Estoque estoque) {
		EstoqueDto estoqueDto = new EstoqueDto();

		estoqueDto.setDisponibilidade(estoque.isDisponibilidade());
		estoqueDto.setEstoqueMaximo(estoque.getEstoqueMaximo());
		estoqueDto.setEstoqueMinimo(estoque.getEstoqueMinimo());
		estoqueDto.setLucro(estoque.getLucro());
		estoqueDto.setPorcentagemLucro(estoque.getPorcentagemLucro());
		estoqueDto.setPrecoAquisicao(estoque.getPrecoAquisicao());
		estoqueDto.setQuantidade(estoque.getQuantidade());

		return estoqueDto;
	}

	@Transactional
	public void removerEstoque(int id) {
		estoqueRepo.deleteById(id);
	}

}
