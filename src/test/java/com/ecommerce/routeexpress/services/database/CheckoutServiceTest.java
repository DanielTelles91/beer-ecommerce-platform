package com.ecommerce.routeexpress.services.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
 * Nenhuma dependência real é usada (banco de dados, envio de e-mail). Todos os
 * repositórios e o EmailService são substituídos por objetos simulados de
 * teste criados com Mockito, que imitam o comportamento dessas
 * dependências sem executá-las de verdade. O objetivo é validar a REGRA DE
 * NEGÓCIO do checkout de forma isolada, sem depender de infraestrutura
 * externa.
 */


@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

	// Cada @Mock cria um objeto FALSO que finge ser essa interface.

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

	// @InjectMocks cria uma instância REAL de CheckoutService (com a lógica de
	// negócio de verdade) e injeta os 7 mocks acima dentro dela, nos campos
	// @Autowired. Estamos testando o código real, só as dependências
	// dele é que são fake.
	@InjectMocks
	private CheckoutService checkoutService;


	private static final int CLIENTE_ID = 1;

	// Objetos que serão recriados do zero antes de CADA teste (@BeforeEach
	// abaixo). Ficam aqui como campos da classe pra qualquer método de teste
	// poder acessá-los e ajustá-los conforme necessário.
	private Carrinho carrinho;
	private CarrinhoItem itemCarrinho;
	private Cerveja cerveja;
	private Estoque estoque;
	private Endereco endereco;
	private Cliente cliente;

	// @BeforeEach roda ANTES de cada @Test, sempre do zero. Isso garante que um
	// teste nunca "vaza" estado pro próximo. Cada teste começa com um cenário
	// limpo e previsível.
	@BeforeEach
	void setUp() {
		// Monta uma cervejaria fake
		Cervejaria cervejaria = new Cervejaria();
		cervejaria.setId(1);
		cervejaria.setCervejaria("Cervejaria Teste");

		// Cerveja fake que será comprada nos testes de sucesso.
		cerveja = new Cerveja();
		cerveja.setId(10);
		cerveja.setRotulo("Cerveja 1");
		cerveja.setCervejaria(cervejaria);
		cerveja.setImagem_1("cerveja1.jpg");

		// Estoque fake dessa cerveja: 5 unidades disponíveis, a R$15,00 cada.
		estoque = new Estoque();
		estoque.setId(100);
		estoque.setCerveja(cerveja);
		estoque.setQuantidade(5);
		estoque.setPreco(15.0);
		estoque.setDisponibilidade(true);

		// Item de carrinho: o cliente quer comprar 2 unidades dessa cerveja.
		itemCarrinho = new CarrinhoItem();
		itemCarrinho.setCerveja(cerveja);
		itemCarrinho.setQuantidade(2);

		// Carrinho do cliente, já com o item acima dentro.
		carrinho = new Carrinho();
		carrinho.setClienteId(CLIENTE_ID);
		carrinho.getItens().add(itemCarrinho);

		// Endereço de entrega fake do cliente.
		endereco = new Endereco();
		endereco.setLogradouro("Rua das Cervejas");
		endereco.setTipo_logradouro("Rua");
		endereco.setLogradouro_numero("123");
		endereco.setBairro("Centro");
		endereco.setCidade("São Paulo");
		endereco.setEstado("SP");
		endereco.setCep("01000-000");

		// Cliente fake, usado principalmente pra pegar o e-mail de confirmação.
		cliente = new Cliente();
		cliente.setId(CLIENTE_ID);
		cliente.setEmail("cliente@teste.com");
	}
	
	

	// ------------------------------------------------------------------
	// (1) finalizarPedido - Caminho Correto (tudo dá certo)
	// ------------------------------------------------------------------

	@Test
	void deveFinalizarPedidoComSucesso() {
		// ===== ARRANGE =====
		// Ensina cada mock a responder de um jeito específico quando o
		// service chamar.

		// Quando perguntarem o carrinho do cliente 1, devolva o carrinho fake.
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));

		// Quando perguntarem os endereços do cliente 1, devolva uma lista com
		// o endereço fake dentro.
		when(enderecoRepo.findByClienteId(CLIENTE_ID)).thenReturn(List.of(endereco));

		// Quando perguntarem o estoque da cerveja de id 10, devolva o estoque
		// fake (5 unidades, R$15).
		when(estoqueRepo.findFirstByCervejaId(cerveja.getId())).thenReturn(Optional.of(estoque));

		// thenAnswer permite escrever uma lógica em vez de sempre devolver
		// o mesmo valor fixo. Pega o Pedido que o service tentou salvar,
		// dá um id nele (como o banco faria de verdade) e devolve esse mesmo
		// objeto, porque o resto do código precisa desse id preenchido.
		when(pedidoRepo.save(any(Pedido.class))).thenAnswer(invocation -> {
			Pedido p = invocation.getArgument(0);
			p.setId(999L);
			return p;
		});

		// Quando perguntarem os dados do cliente 1, devolva o cliente fake
		// (que tem e-mail cadastrado)
		when(clientesRepositorio.findById(CLIENTE_ID)).thenReturn(Optional.of(cliente));

		// ===== ACT =====
		// Executa código de PRODUÇÃO de verdade. Tudo que veio
		// antes foi só preparação pra essa chamada funcionar sem banco real.
		PedidoDto resultado = checkoutService.finalizarPedido(CLIENTE_ID);

		// ===== ASSERT =====
		// assertThat confere o VALOR DE RETORNO do método.
		assertThat(resultado).isNotNull();
		assertThat(resultado.getTotal()).isEqualTo(30.0); // 2 unidades * 15.0
		assertThat(resultado.getItens()).hasSize(1);
		assertThat(resultado.getItens().get(0).getRotulo()).isEqualTo("Cerveja 1");

		// ArgumentCaptor funciona como uma "rede de pesca": em vez de só
		// confirmar QUE estoqueRepo.save(...) foi chamado, ele CAPTURA o
		// objeto que foi passado pra dentro do save(), pra inspecionar.
		ArgumentCaptor<Estoque> estoqueCaptor = ArgumentCaptor.forClass(Estoque.class);
		verify(estoqueRepo).save(estoqueCaptor.capture());
		// Confirma que o estoque salvo ficou com 3 unidades (5 - 2 compradas)
		// e continua disponível pra venda.
		assertThat(estoqueCaptor.getValue().getQuantidade()).isEqualTo(3);
		assertThat(estoqueCaptor.getValue().isDisponibilidade()).isTrue();

		// Confirma que o item comprado saiu do carrinho e que essa mudança foi
		// persistida (verify sem argumento extra = "foi chamado exatamente 1 vez").
		assertThat(carrinho.getItens()).isEmpty();
		verify(carrinhoRepo).save(carrinho);

		// Confirma que um histórico de status do pedido foi criado, sem se
		// importar com o conteúdo exato (any()), só que aconteceu.
		verify(historicoRepo).save(any());

		// Confirma que o e-mail de confirmação foi "enviado" (chamado no mock)
		// pro endereço certo.
		verify(emailService).enviarEmailConfirmacaoPedido(eq("cliente@teste.com"), any(PedidoDto.class));
	}

	@Test
	void deveMarcarIndisponivelQuandoEstoqueZera() {
		// Sobrescreve o estoque criado no setUp() (5 unidades) pra 2 - igual à
		// quantidade pedida no carrinho. Assim a compra consome o estoque inteiro.
		estoque.setQuantidade(2);

		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));
		when(enderecoRepo.findByClienteId(CLIENTE_ID)).thenReturn(List.of(endereco));
		when(estoqueRepo.findFirstByCervejaId(cerveja.getId())).thenReturn(Optional.of(estoque));
	
		when(pedidoRepo.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(clientesRepositorio.findById(CLIENTE_ID)).thenReturn(Optional.of(cliente));

		checkoutService.finalizarPedido(CLIENTE_ID);

		// Regra de negócio testada aqui: quando o estoque chega a 0 depois da
		// compra, o produto deve ser marcado automaticamente como indisponível
		// (senão continuaria aparecendo como comprável na loja com 0 unidades).
		ArgumentCaptor<Estoque> estoqueCaptor = ArgumentCaptor.forClass(Estoque.class);
		verify(estoqueRepo).save(estoqueCaptor.capture());
		assertThat(estoqueCaptor.getValue().getQuantidade()).isZero();
		assertThat(estoqueCaptor.getValue().isDisponibilidade()).isFalse();
	}

	// ------------------------------------------------------------------
	// finalizarPedido - validações que abortam o checkout
	// ------------------------------------------------------------------

	@Test
	void deveLancarExcecaoQuandoCarrinhoNaoExiste() {
		// Optional.empty() = "caixa vazia": simula que não existe carrinho
		// nenhum pra esse cliente no banco.
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.empty());

		// assertThatThrownBy roda o código dentro do lambda e espera que ele
		// LANCE uma exceção. isInstanceOf confere o tipo; hasMessageContaining
		// confere um trecho da mensagem.
		assertThatThrownBy(() -> checkoutService.finalizarPedido(CLIENTE_ID)).isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Carrinho não encontrado");

		// never() confirma que esse método NUNCA foi chamado, ou seja, o
		// checkout abortou antes de tentar salvar qualquer coisa.
		verify(pedidoRepo, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoCarrinhoVazio() {
		// Diferença pro teste anterior: aqui o carrinho EXISTE (Optional.of),
		// só que está sem nenhum item dentro.
		carrinho.getItens().clear();
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));

		assertThatThrownBy(() -> checkoutService.finalizarPedido(CLIENTE_ID)).isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Carrinho está vazio");

		verify(pedidoRepo, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoSemEndereco() {
		// Precisamos mockar carrinhoRepo mesmo esse teste não sendo "sobre"
		// carrinho: o CheckoutService só chega a checar o endereço DEPOIS de
		// validar que o carrinho existe e tem itens. Sem isso, o mock
		// devolveria null e o teste quebraria antes da parte que queremos testar.
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));
		// List.of() = lista vazia: simula "cliente sem nenhum endereço cadastrado".
		when(enderecoRepo.findByClienteId(CLIENTE_ID)).thenReturn(List.of());

		assertThatThrownBy(() -> checkoutService.finalizarPedido(CLIENTE_ID)).isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Nenhum endereço cadastrado");

		verify(pedidoRepo, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoEstoqueInsuficiente() {
		// Pega o item que o setUp() criou (2 unidades) e força pra 10, mais
		// do que as 5 disponíveis em estoque.
		itemCarrinho.setQuantidade(10);
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));
		when(enderecoRepo.findByClienteId(CLIENTE_ID)).thenReturn(List.of(endereco));
		when(estoqueRepo.findFirstByCervejaId(cerveja.getId())).thenReturn(Optional.of(estoque));

		// A mensagem real é montada no CheckoutService:
		// cerveja.getRotulo() + ": você pediu " + qtd + " unidade(s), mas só
		// temos " + estoque.getQuantidade() + " disponível(is)"
		// Por isso conferimos o rótulo "Cerveja 1" (o mesmo do setUp()) e o
		// trecho da mensagem de quantidade. 
		assertThatThrownBy(() -> checkoutService.finalizarPedido(CLIENTE_ID)).isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Cerveja 1").hasMessageContaining("só temos 5 disponível");

		// Checkout é "tudo ou nada": se falhar, NADA pode ter sido salvo em
		// lugar nenhum - nem pedido, nem estoque, nem carrinho, nem e-mail.
		verify(pedidoRepo, never()).save(any());
		verify(estoqueRepo, never()).save(any());
		verify(carrinhoRepo, never()).save(any());
		verify(emailService, never()).enviarEmailConfirmacaoPedido(any(), any());
	}

	@Test
	void deveLancarExcecaoQuandoProdutoIndisponivel() {
		// Estoque EXISTE, mas está marcado como indisponível pra venda.
		estoque.setDisponibilidade(false);
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));
		when(enderecoRepo.findByClienteId(CLIENTE_ID)).thenReturn(List.of(endereco));
		when(estoqueRepo.findFirstByCervejaId(cerveja.getId())).thenReturn(Optional.of(estoque));

		assertThatThrownBy(() -> checkoutService.finalizarPedido(CLIENTE_ID)).isInstanceOf(RuntimeException.class)
				.hasMessageContaining("produto indisponível");

		verify(pedidoRepo, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoEstoqueNaoExiste() {
		// Diferença pro teste anterior: aqui o estoque nem EXISTE no banco
		// (Optional.empty()), em vez de existir e estar indisponível. No
		// código de produção os dois casos caem no mesmo "if" e geram a
		// mesma mensagem de erro.
		when(carrinhoRepo.findByClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrinho));
		when(enderecoRepo.findByClienteId(CLIENTE_ID)).thenReturn(List.of(endereco));
		when(estoqueRepo.findFirstByCervejaId(cerveja.getId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> checkoutService.finalizarPedido(CLIENTE_ID)).isInstanceOf(RuntimeException.class)
				.hasMessageContaining("produto indisponível");

		verify(pedidoRepo, never()).save(any());
	}

	// ------------------------------------------------------------------
	// listarPedidos. método simples, sem regra de negócio complexa, só
	// busca no repositório e converte pra DTO.
	// ------------------------------------------------------------------

	@Test
	void deveListarPedidosDoCliente() {
		// Monta um pedido "já existente no banco"
		Pedido pedido = new Pedido();
		pedido.setId(1L);
		pedido.setClienteId(CLIENTE_ID);
		pedido.setTotal(30.0);
		pedido.setEnderecoLogradouro("Rua das Cervejas");
		pedido.setEnderecoTipoLogradouro("Rua");
		pedido.setEnderecoNumero("123");
		pedido.setEnderecoBairro("Centro");
		pedido.setEnderecoCidade("São Paulo");
		pedido.setEnderecoEstado("SP");
		pedido.setEnderecoCep("01000-000");

		// Quando buscarem os pedidos do cliente 1 ordenados por data, devolva
		// uma lista com esse pedido fake dentro.
		when(pedidoRepo.findByClienteIdOrderByDataPedidoDesc(CLIENTE_ID)).thenReturn(List.of(pedido));

		List<PedidoDto> resultado = checkoutService.listarPedidos(CLIENTE_ID);

		assertThat(resultado).hasSize(1);
		assertThat(resultado.get(0).getId()).isEqualTo(1L);
		assertThat(resultado.get(0).getTotal()).isEqualTo(30.0);
		// times(1) é a forma explícita de dizer "exatamente 1 vez
		verify(pedidoRepo, times(1)).findByClienteIdOrderByDataPedidoDesc(CLIENTE_ID);
	}

	@Test
	void deveRetornarListaVaziaQuandoSemPedidos() {
		// anyInt(): não importa qual número me passarem,
		// responda sempre assim. só queremos
		// confirmar que uma lista vazia do banco vira lista vazia de DTOs.
		when(pedidoRepo.findByClienteIdOrderByDataPedidoDesc(anyInt())).thenReturn(List.of());

		List<PedidoDto> resultado = checkoutService.listarPedidos(CLIENTE_ID);

		assertThat(resultado).isEmpty();
	}

	// ------------------------------------------------------------------
	// finalizarPedido - condição de corrida (BUG CONHECIDO, ainda sem
	// correção). Este teste está aqui de propósito, ANTES da correção, pra
	// provar o problema. Quando o CheckoutService for corrigido (com lock
	// otimista/pessimista no Estoque), este teste deve voltar a falhar do
	// jeito "certo": um dos dois clientes deve receber uma exceção de
	// estoque insuficiente, em vez dos dois conseguirem comprar.
	// ------------------------------------------------------------------

	@Test
	void deveSobrevenderQuandoDoisClientesFinalizamAoMesmoTempo() throws Exception {
		// ===== CENÁRIO =====
		// Estoque com 5 unidades. Dois clientes DIFERENTES (1 e 2), cada um
		// com seu próprio carrinho, mas comprando a MESMA cerveja 3
		// unidades cada. Juntos, pedem 6 unidades de um estoque de 5.
		estoque.setQuantidade(5);

		Carrinho carrinhoCliente1 = new Carrinho();
		carrinhoCliente1.setClienteId(1);
		CarrinhoItem itemCliente1 = new CarrinhoItem();
		itemCliente1.setCerveja(cerveja);
		itemCliente1.setQuantidade(3);
		carrinhoCliente1.getItens().add(itemCliente1);

		Carrinho carrinhoCliente2 = new Carrinho();
		carrinhoCliente2.setClienteId(2);
		CarrinhoItem itemCliente2 = new CarrinhoItem();
		itemCliente2.setCerveja(cerveja);
		itemCliente2.setQuantidade(3);
		carrinhoCliente2.getItens().add(itemCliente2);

		when(carrinhoRepo.findByClienteId(1)).thenReturn(Optional.of(carrinhoCliente1));
		when(carrinhoRepo.findByClienteId(2)).thenReturn(Optional.of(carrinhoCliente2));
		when(enderecoRepo.findByClienteId(anyInt())).thenReturn(List.of(endereco));
		when(pedidoRepo.save(any(Pedido.class))).thenAnswer(invocation -> {
			Pedido p = invocation.getArgument(0);
			p.setId(1L);
			return p;
		});
		when(clientesRepositorio.findById(anyInt())).thenReturn(Optional.of(cliente));

		// ===== A PARTE QUE FORÇA A CONDIÇÃO DE CORRIDA =====
		//
		// O finalizarPedido chama estoqueRepo.findFirstByCervejaId DUAS
		// vezes: uma pra VALIDAR se tem estoque suficiente, outra pra DEBITAR
		// depois de já ter decidido que pode vender. Ao todo, com 2 clientes
		// rodando ao mesmo tempo, esse método vai ser chamado 4 vezes (2
		// validações + 2 débitos).
		//
		// AtomicInteger conta quantas vezes o mock já foi chamado, de forma
		// segura mesmo sendo acessado por 2 threads ao mesmo tempo (um "int"
		// comum poderia contar errado nessa situação).
		AtomicInteger chamadas = new AtomicInteger(0);

		// CyclicBarrier é uma "porteira": quando você chama barreira.await(),
		// a thread FICA PARADA ali até que o número combinado de threads
		// (aqui, 2) tenha chegado nesse mesmo ponto. Só quando as duas
		// chegam, a porteira libera as duas ao mesmo tempo.
		//
		// Usamos DUAS porteiras: uma seguinte as 2 chamadas de VALIDAÇÃO
		// (uma de cada thread), outra segurando as 2 chamadas de DÉBITO. Isso
		// garante que: (1) os dois clientes leem o MESMO estoque (5) antes de
		// qualquer um debitar, e (2) os dois debitam também vendo o mesmo
		// valor original (5), simulando o pior caso da condição de corrida.
		CyclicBarrier barreiraValidacao = new CyclicBarrier(2);
		CyclicBarrier barreiraDebito = new CyclicBarrier(2);

		when(estoqueRepo.findFirstByCervejaId(anyInt())).thenAnswer(invocation -> {
			int indice = chamadas.getAndIncrement();
			if (indice == 0 || indice == 1) {
				barreiraValidacao.await(); // segura até as 2 primeiras chamadas chegarem
			} else if (indice == 2 || indice == 3) {
				barreiraDebito.await(); // segura até as 2 chamadas seguintes chegarem
			}
			return Optional.of(estoque);
		});

		// ===== ACT =====
		// ExecutorService cria um "pool" de threads reais. Callable
		// é como Runnable, mas pode devolver um valor (nesse caso, o
		// PedidoDto) e pode lançar exceção, que fica guardada no Future.
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Callable<PedidoDto> checkoutCliente1 = () -> checkoutService.finalizarPedido(1);
		Callable<PedidoDto> checkoutCliente2 = () -> checkoutService.finalizarPedido(2);

		// submit() dispara a execução em paralelo e devolve na hora um
		// Future - uma "promessa" do resultado, que ainda não existe.
		Future<PedidoDto> futuro1 = executor.submit(checkoutCliente1);
		Future<PedidoDto> futuro2 = executor.submit(checkoutCliente2);

		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.SECONDS);

		// future.get() bloqueia até o resultado ficar pronto. Se a thread
		// lançou uma exceção lá dentro, get() relança ela aqui (embrulhada em
		// ExecutionException), por isso capturamos pra checar se cada
		// checkout terminou com sucesso ou com erro.
		Exception erroCliente1 = null;
		Exception erroCliente2 = null;
		try {
			futuro1.get();
		} catch (Exception e) {
			erroCliente1 = e;
		}
		try {
			futuro2.get();
		} catch (Exception e) {
			erroCliente2 = e;
		}

		// ===== ASSERT: aqui verificamos o problema =====
		//
		// O correto seria apenas um cliente conseguir comprar.
		// O outro deveria receber erro de estoque insuficiente.
		//
		// Por causa do bug, os dois clientes conseguem comprar.
		// Os dois leem o estoque como 5 e tentam comprar 3 unidades.
		// No final, o estoque pode ficar com um valor incorreto.
		//
		// Isso mostra que os dois pedidos foram aceitos mesmo sem
		// existir estoque suficiente para os dois.
		
		assertThat(erroCliente1).as("cliente 1 não deveria ter recebido erro (bug: os dois passam)").isNull();
		assertThat(erroCliente2).as("cliente 2 não deveria ter recebido erro (bug: os dois passam)").isNull();

		// O estoque final pode ficar em 2 ou -1 dependendo de como 
		// as threads foram executadas pelo sistema.
		//
		// Os dois valores mostram que existe um problema no controle
		// do estoque quando as duas compras acontecem ao mesmo tempo.
		//
		// O correto seria uma compra ser aprovada e a outra ser rejeitada.
		assertThat(estoque.getQuantidade())
				.as("estoque final ficou incoerente: 2 pedidos de 3un. cada saíram de um estoque de 5un.")
				.isIn(2, -1);

		// Depois que o CheckoutService for corrigido, uma das compras
		// deve receber erro de estoque insuficiente.
		//
		// O estoque também não deve ficar negativo.
		// Este teste ajuda a mostrar o problema atual e verificar
		// se ele foi corrigido depois.
	}
}