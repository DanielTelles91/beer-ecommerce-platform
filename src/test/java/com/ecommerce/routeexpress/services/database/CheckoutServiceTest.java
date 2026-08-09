package com.ecommerce.routeexpress.services.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.routeexpress.dto.PedidoDto;
import com.ecommerce.routeexpress.models.Carrinho;
import com.ecommerce.routeexpress.models.CarrinhoItem;
import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.Cervejaria;
import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.models.Endereco;
import com.ecommerce.routeexpress.models.Estoque;
import com.ecommerce.routeexpress.models.Pedido;
import com.ecommerce.routeexpress.services.CarrinhoRepositorio;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.EnderecosRepositorio;
import com.ecommerce.routeexpress.services.EstoquesRepositorio;
import com.ecommerce.routeexpress.services.PedidoRepositorio;
import com.ecommerce.routeexpress.services.PedidoStatusHistoricoRepositorio;
import com.ecommerce.routeexpress.services.email.EmailService;

/**
 * Testes unitários de CheckoutService.
 *
 * Nenhuma dependência real é usada (banco, e-mail). Todos os
 * repositórios e o EmailService são mockados com Mockito. O objetivo é
 * validar a REGRA DE NEGÓCIO do checkout de forma isolada e rápida.
 */
@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
 
	@Mock
	private CarrinhoRepositorio carrinhoRepo;
	@Mock
	private EnderecosRepositorio enderecoRepo;
	@Mock
	private EstoquesRepositorio estoqueRepo;
	@Mock
	private PedidoRepositorio pedidoRepo;
	@Mock
	private EmailService emailService;
	@Mock
	private ClientesRepositorio clientesRepositorio;
	@Mock
	private PedidoStatusHistoricoRepositorio historicoRepo;
 
	@InjectMocks
	private CheckoutService checkoutService;
 
	private static final int CLIENTE_ID = 1;
 
	private Carrinho carrinho;
	private CarrinhoItem itemCarrinho;
	private Cerveja cerveja;
	private Estoque estoque;
	private Endereco endereco;
	private Cliente cliente;
 
	@BeforeEach
	void setUp() {
		Cervejaria cervejaria = new Cervejaria();
		cervejaria.setId(1);
		cervejaria.setCervejaria("Cervejaria Teste");
 
		cerveja = new Cerveja();
		cerveja.setId(10);
		cerveja.setRotulo("Cerveja 1");
		cerveja.setCervejaria(cervejaria);
		cerveja.setImagem_1("cerveja1.jpg");
 
		estoque = new Estoque();
		estoque.setId(100);
		estoque.setCerveja(cerveja);
		estoque.setQuantidade(5);
		estoque.setPreco(15.0);
		estoque.setDisponibilidade(true);
 
		itemCarrinho = new CarrinhoItem();
		itemCarrinho.setCerveja(cerveja);
		itemCarrinho.setQuantidade(2);
 
		carrinho = new Carrinho();
		carrinho.setClienteId(CLIENTE_ID);
		carrinho.getItens().add(itemCarrinho);
 
		endereco = new Endereco();
		endereco.setLogradouro("Rua das Cervejas");
		endereco.setTipo_logradouro("Rua");
		endereco.setLogradouro_numero("123");
		endereco.setBairro("Centro");
		endereco.setCidade("São Paulo");
		endereco.setEstado("SP");
		endereco.setCep("01000-000");
 
		cliente = new Cliente();
		cliente.setId(CLIENTE_ID);
		cliente.setEmail("cliente@teste.com");
	}
 
	@Test
	void deveFinalizarPedidoComSucesso() {
		// ARRANGE: ensina cada mock a responder quando o service chamar
 
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));
		when(enderecoRepo.findByClienteId(CLIENTE_ID)).thenReturn(List.of(endereco));
		when(estoqueRepo.findFirstByCervejaId(cerveja.getId())).thenReturn(Optional.of(estoque));
		when(pedidoRepo.save(any(Pedido.class))).thenAnswer(invocation -> {
			Pedido p = invocation.getArgument(0);
			p.setId(999L);
			return p;
		});
		when(clientesRepositorio.findById(CLIENTE_ID)).thenReturn(Optional.of(cliente));
 
		// ACT: executa o método real que estamos testando
 
		PedidoDto resultado = checkoutService.finalizarPedido(CLIENTE_ID);
 
		// ASSERT: confere se o resultado e os efeitos colaterais foram os esperados
 
		assertThat(resultado).isNotNull();
		assertThat(resultado.getTotal()).isEqualTo(30.0); // 2 unidades x 15.0
		assertThat(resultado.getItens()).hasSize(1);
		assertThat(resultado.getItens().get(0).getRotulo()).isEqualTo("Cerveja 1");
 
		ArgumentCaptor<Estoque> estoqueCaptor = ArgumentCaptor.forClass(Estoque.class);
		verify(estoqueRepo).save(estoqueCaptor.capture());
		assertThat(estoqueCaptor.getValue().getQuantidade()).isEqualTo(3);
		assertThat(estoqueCaptor.getValue().isDisponibilidade()).isTrue();
 
		assertThat(carrinho.getItens()).isEmpty();
		verify(carrinhoRepo).save(carrinho);
 
		verify(historicoRepo).save(any());
 
		verify(emailService).enviarEmailConfirmacaoPedido(eq("cliente@teste.com"), any(PedidoDto.class));
	}
}