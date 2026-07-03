package com.ecommerce.routeexpress.services.database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.routeexpress.dto.ItemPedidoDto;
import com.ecommerce.routeexpress.dto.PedidoDto;
import com.ecommerce.routeexpress.models.*;
import com.ecommerce.routeexpress.services.*;
import com.ecommerce.routeexpress.services.email.EmailService;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
public class CheckoutService {

	@Autowired
	private CarrinhoRepositorio carrinhoRepo;
	@Autowired
	private EnderecosRepositorio enderecoRepo;
	@Autowired
	private EstoquesRepositorio estoqueRepo;
	@Autowired
	private PedidoRepositorio pedidoRepo;
	@Autowired
	private EmailService emailService;
	@Autowired
	private ClientesRepositorio clientesRepositorio;

	@Transactional
	public PedidoDto finalizarPedido(int clienteId) {

		// 1. Busca o carrinho do cliente
		Carrinho carrinho = carrinhoRepo.findByClienteId(clienteId)
				.orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

		if (carrinho.getItens().isEmpty()) {
			throw new RuntimeException("Carrinho está vazio");
		}

		// 2. Busca o endereço do cliente
		List<Endereco> enderecos = enderecoRepo.findByClienteId(clienteId);
		if (enderecos.isEmpty()) {
			throw new RuntimeException("Nenhum endereço cadastrado");
		}
		Endereco endereco = enderecos.get(0);

		// 3. Verifica estoque e monta os itens do pedido
		List<ItemPedido> itensPedido = new ArrayList<>();
		List<String> errosEstoque = new ArrayList<>();

		for (CarrinhoItem item : carrinho.getItens()) {
			Cerveja cerveja = item.getCerveja();
			Estoque estoque = estoqueRepo.findFirstByCervejaId(cerveja.getId()).orElse(null);

			if (estoque == null || !estoque.isDisponibilidade()) {
				errosEstoque.add(cerveja.getRotulo() + ": produto indisponível");
				continue;
			}

			if (estoque.getQuantidade() < item.getQuantidade()) {
				errosEstoque.add(cerveja.getRotulo() + ": você pediu " + item.getQuantidade()
						+ " unidade(s), mas só temos " + estoque.getQuantidade() + " disponível(is)");
				continue;
			}

			// Snapshot dos dados da cerveja
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setCervejaId(cerveja.getId());
			itemPedido.setCervejariaId(cerveja.getCervejaria().getId());
			itemPedido.setRotulo(cerveja.getRotulo());
			itemPedido.setNomeCervejaria(cerveja.getCervejaria().getCervejaria());
			itemPedido.setPrecoUnitario(estoque.getPreco());
			itemPedido.setImagem(cerveja.getImagem_1());
			itemPedido.setQuantidade(item.getQuantidade());
			itemPedido.setSubtotal(estoque.getPreco() * item.getQuantidade());

			itensPedido.add(itemPedido);
		}

		// Se tiver qualquer erro de estoque, aborta tudo
		if (!errosEstoque.isEmpty()) {
			throw new RuntimeException("Problemas no estoque: " + String.join("; ", errosEstoque));
		}

		// 4. Cria o pedido com snapshot do endereço
		Pedido pedido = new Pedido();
		pedido.setClienteId(clienteId);
		pedido.setEnderecoLogradouro(endereco.getLogradouro());
		pedido.setEnderecoTipoLogradouro(endereco.getTipo_logradouro());
		pedido.setEnderecoNumero(endereco.getLogradouro_numero());
		pedido.setEnderecoComplemento(endereco.getComplemento());
		pedido.setEnderecoBairro(endereco.getBairro());
		pedido.setEnderecoCidade(endereco.getCidade());
		pedido.setEnderecoEstado(endereco.getEstado());
		pedido.setEnderecoCep(endereco.getCep());

		// Vincula os itens ao pedido
		for (ItemPedido item : itensPedido) {
			item.setPedido(pedido);
		}
		pedido.setItens(itensPedido);
		pedido.setTotal(itensPedido.stream().mapToDouble(ItemPedido::getSubtotal).sum());

		Pedido salvo = pedidoRepo.save(pedido);

		// 5. Debita o estoque e atualiza disponibilidade se necessário
		for (ItemPedido item : itensPedido) {
			Estoque estoque = estoqueRepo.findFirstByCervejaId(item.getCervejaId()).get();
			estoque.setQuantidade(estoque.getQuantidade() - item.getQuantidade());
			if (estoque.getQuantidade() <= 0) {
				estoque.setDisponibilidade(false);
			}
			estoqueRepo.save(estoque);
		}

		// 6. Esvazia o carrinho
		carrinho.getItens().clear();
		carrinhoRepo.save(carrinho);

		// Monta o DTO e envia o e-mail de confirmação
		PedidoDto pedidoDto = montarDto(salvo);

		// Busca o e-mail do cliente pra enviar a confirmação
		clientesRepositorio.findById(clienteId)
				.ifPresent(cliente -> emailService.enviarEmailConfirmacaoPedido(cliente.getEmail(), pedidoDto));

		return montarDto(salvo);
	}

	public List<PedidoDto> listarPedidos(int clienteId) {
		return pedidoRepo.findByClienteIdOrderByDataPedidoDesc(clienteId).stream().map(this::montarDto)
				.collect(Collectors.toList());
	}

	private PedidoDto montarDto(Pedido pedido) {
		PedidoDto dto = new PedidoDto();
		dto.setId(pedido.getId());
		dto.setDataPedido(pedido.getDataPedido());
		dto.setStatus(pedido.getStatus());
		dto.setTotal(pedido.getTotal());
		dto.setEnderecoCompleto(pedido.getEnderecoTipoLogradouro() + " " + pedido.getEnderecoLogradouro() + ", "
				+ pedido.getEnderecoNumero()
				+ (pedido.getEnderecoComplemento() != null && !pedido.getEnderecoComplemento().isBlank()
						? " — " + pedido.getEnderecoComplemento()
						: "")
				+ ", " + pedido.getEnderecoBairro() + ", " + pedido.getEnderecoCidade() + " - "
				+ pedido.getEnderecoEstado() + " | CEP: " + pedido.getEnderecoCep());

		dto.setItens(pedido.getItens().stream().map(i -> {
			ItemPedidoDto itemDto = new ItemPedidoDto();
			itemDto.setId(i.getId());
			itemDto.setCervejaId(i.getCervejaId());
			itemDto.setCervejaId(i.getCervejaId());
			itemDto.setCervejariaId(i.getCervejariaId());
			itemDto.setRotulo(i.getRotulo());
			itemDto.setNomeCervejaria(i.getNomeCervejaria());
			itemDto.setPrecoUnitario(i.getPrecoUnitario());
			itemDto.setImagem(i.getImagem());
			itemDto.setQuantidade(i.getQuantidade());
			itemDto.setSubtotal(i.getSubtotal());
			return itemDto;
		}).collect(Collectors.toList()));

		return dto;
	}
}
