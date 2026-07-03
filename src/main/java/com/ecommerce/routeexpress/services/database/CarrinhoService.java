package com.ecommerce.routeexpress.services.database;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.services.EstoquesRepositorio;
import com.ecommerce.routeexpress.models.Estoque;
import com.ecommerce.routeexpress.dto.CarrinhoDto;
import com.ecommerce.routeexpress.dto.CarrinhoItemDto;
import com.ecommerce.routeexpress.models.Carrinho;
import com.ecommerce.routeexpress.models.CarrinhoItem;
import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.services.CarrinhoItemRepositorio;
import com.ecommerce.routeexpress.services.CarrinhoRepositorio;
import com.ecommerce.routeexpress.services.CervejasRepositorio;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
public class CarrinhoService {

	@Autowired
	private CarrinhoRepositorio carrinhoRepo;
	@Autowired
	private CarrinhoItemRepositorio itemRepo;
	@Autowired
	private CervejasRepositorio cervejaRepo;
	@Autowired
	private EstoquesRepositorio estoqueRepo;

	private Carrinho buscarOuCriarCarrinho(String sessionId) {
		Carrinho carrinho = carrinhoRepo.findBySessionId(sessionId).orElseGet(() -> {
			Carrinho novo = new Carrinho();
			novo.setSessionId(sessionId);
			return novo;
		});
		carrinho.setDataAtualizacao(LocalDateTime.now());
		return carrinhoRepo.save(carrinho);
	}

	public CarrinhoDto buscarCarrinho(String sessionId) {
		Carrinho carrinho = buscarOuCriarCarrinho(sessionId);
		return montarDto(carrinho);
	}

	public CarrinhoDto adicionarItem(String sessionId, int cervejaId, int quantidade) {
		Carrinho carrinho = buscarOuCriarCarrinho(sessionId);
		Cerveja cerveja = cervejaRepo.findById(cervejaId)
				.orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));

		CarrinhoItem item = carrinho.getItens().stream().filter(i -> i.getCerveja().getId() == cervejaId).findFirst()
				.orElse(null);

		if (item != null) {
			item.setQuantidade(item.getQuantidade() + quantidade);
		} else {
			item = new CarrinhoItem();
			item.setCarrinho(carrinho);
			item.setCerveja(cerveja);
			item.setQuantidade(quantidade);
			carrinho.getItens().add(item);
		}

		carrinhoRepo.save(carrinho);
		return montarDto(carrinho);
	}

	public CarrinhoDto atualizarQuantidade(String sessionId, Long itemId, int quantidade) {
		Carrinho carrinho = buscarOuCriarCarrinho(sessionId);
		CarrinhoItem item = itemRepo.findById(itemId).orElseThrow(() -> new RuntimeException("Item não encontrado"));

		if (quantidade <= 0) {
			carrinho.getItens().remove(item);
			itemRepo.delete(item);
		} else {
			item.setQuantidade(quantidade);
		}

		carrinhoRepo.save(carrinho);
		return montarDto(carrinho);
	}

	public CarrinhoDto removerItem(String sessionId, Long itemId) {
		return atualizarQuantidade(sessionId, itemId, 0);
	}

	private CarrinhoDto montarDto(Carrinho carrinho) {
		List<CarrinhoItemDto> itens = carrinho.getItens().stream().map(i -> {
			Estoque estoque = estoqueRepo.findFirstByCervejaId(i.getCerveja().getId()).orElse(null);

			double preco = estoque != null ? estoque.getPreco() : 0.0;
			int estoqueDisponivel = estoque != null ? estoque.getQuantidade() : 0;

			return new CarrinhoItemDto(i.getId(), i.getCerveja().getId(), i.getCerveja().getRotulo(), preco,
					i.getQuantidade(), estoqueDisponivel);
		}).collect(Collectors.toList());
		return new CarrinhoDto(itens);
	}

	public CarrinhoDto mergeCarrinho(String sessionId, int clienteId) {
		Carrinho carrinhoVisitante = carrinhoRepo.findBySessionId(sessionId).orElse(null);
		Carrinho carrinhoCliente = carrinhoRepo.findByClienteId(clienteId).orElse(null);

		// Sem carrinho de visitante: só garante que o cliente tem algum carrinho
		if (carrinhoVisitante == null) {
			if (carrinhoCliente == null) {
				carrinhoCliente = new Carrinho();
				carrinhoCliente.setSessionId(sessionId);
				carrinhoCliente.setClienteId(clienteId);
				carrinhoCliente.setDataAtualizacao(LocalDateTime.now());
				carrinhoRepo.save(carrinhoCliente);
			}
			return montarDto(carrinhoCliente);
		}

		// Cliente ainda não tinha carrinho: adota o carrinho de visitante atual
		if (carrinhoCliente == null) {
			carrinhoVisitante.setClienteId(clienteId);
			carrinhoVisitante.setDataAtualizacao(LocalDateTime.now());
			carrinhoRepo.save(carrinhoVisitante);
			return montarDto(carrinhoVisitante);
		}

		// Já é o mesmo carrinho (login repetido no mesmo dispositivo)
		if (carrinhoVisitante.getId().equals(carrinhoCliente.getId())) {
			return montarDto(carrinhoCliente);
		}

		// Mescla os itens do carrinho de visitante no carrinho já existente do cliente
		for (CarrinhoItem itemVisitante : carrinhoVisitante.getItens()) {
			CarrinhoItem itemExistente = carrinhoCliente.getItens().stream()
					.filter(i -> i.getCerveja().getId() == itemVisitante.getCerveja().getId()).findFirst().orElse(null);

			if (itemExistente != null) {
				itemExistente.setQuantidade(itemExistente.getQuantidade() + itemVisitante.getQuantidade());
			} else {
				CarrinhoItem novoItem = new CarrinhoItem();
				novoItem.setCarrinho(carrinhoCliente);
				novoItem.setCerveja(itemVisitante.getCerveja());
				novoItem.setQuantidade(itemVisitante.getQuantidade());
				carrinhoCliente.getItens().add(novoItem);
			}
		}

		carrinhoCliente.setDataAtualizacao(LocalDateTime.now());
		carrinhoRepo.save(carrinhoCliente);
		carrinhoRepo.delete(carrinhoVisitante); // cascade remove os itens dele também

		return montarDto(carrinhoCliente);
	}
}
