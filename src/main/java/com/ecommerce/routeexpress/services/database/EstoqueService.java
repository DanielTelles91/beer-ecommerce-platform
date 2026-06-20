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
	public void criaEstoque(int cervejaId, int quantidade, double porcentagemLucro, int estoqueMinimo,
			int estoqueMaximo, double precoAquisicao, boolean disponibilidade) { // Creates a new Estoque
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
		estoque.calcularPrecoFinal();

		estoqueRepo.save(estoque);
	}

	public void updateEstoque(int id, EstoqueDto dto) { // Updates a estoque based on the DTO

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
		estoque.calcularPrecoFinal();
		estoqueRepo.save(estoque);
	}

	@Transactional
	public void deleteEstoqueById(int id) { // Deleta estoque by id
		estoqueRepo.deleteById(id);
	}

	public Estoque findById(int id) { // Finds a Cerveja by ID. Throws RuntimeException if not found.
		return estoqueRepo.findById(id).orElseThrow(() -> new RuntimeException("Estoque not found"));
	}

	public EstoqueDto mapToDto(Estoque estoque) { // Maps a Estoque entity to a EstoqueDto.
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

}
