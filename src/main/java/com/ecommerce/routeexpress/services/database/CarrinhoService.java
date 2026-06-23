package com.ecommerce.routeexpress.services.database;

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
		return carrinhoRepo.findBySessionId(sessionId).orElseGet(() -> {
			Carrinho novo = new Carrinho();
			novo.setSessionId(sessionId);
			return carrinhoRepo.save(novo);
		});
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
			double preco = estoqueRepo.findFirstByCervejaId(i.getCerveja().getId()).map(Estoque::getPreco).orElse(0.0);
			return new CarrinhoItemDto(i.getId(), i.getCerveja().getId(), i.getCerveja().getRotulo(), preco,
					i.getQuantidade());
		}).collect(Collectors.toList());
		return new CarrinhoDto(itens);
	}
}
