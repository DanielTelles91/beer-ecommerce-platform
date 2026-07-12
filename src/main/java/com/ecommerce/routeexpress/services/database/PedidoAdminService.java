package com.ecommerce.routeexpress.services.database;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.models.Cliente;
import com.ecommerce.routeexpress.models.Pedido;
import com.ecommerce.routeexpress.models.PedidoStatusHistorico;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.PedidoRepositorio;
import com.ecommerce.routeexpress.services.PedidoStatusHistoricoRepositorio;
import com.ecommerce.routeexpress.services.email.EmailService;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
public class PedidoAdminService {

	@Autowired
	private PedidoRepositorio pedidoRepo;
	@Autowired
	private ClientesRepositorio clienteRepo;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PedidoStatusHistoricoRepositorio historicoRepo;

	private static final List<String> STATUS_VALIDOS = List.of("CONFIRMADO", "SEPARANDO_PRODUTOS", "ENVIADO",
			"ENTREGUE", "CANCELADO");

	public List<Map<String, Object>> listarTodos(String filtroStatus, String busca, String dataInicio, String dataFim) {
		List<Pedido> pedidos = pedidoRepo.buscarComFiltros(
				(filtroStatus == null || filtroStatus.isBlank()) ? null : filtroStatus,
				(dataInicio == null || dataInicio.isBlank()) ? null : dataInicio + " 00:00:00",
				(dataFim == null || dataFim.isBlank()) ? null : dataFim + " 23:59:59",
				(busca == null || busca.isBlank()) ? null : busca);

		return pedidos.stream().map(p -> {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("id", p.getId());
			map.put("clienteId", p.getClienteId());
			map.put("nomeCliente", buscarNomeCliente(p.getClienteId()));
			map.put("dataPedido", p.getDataPedido());
			map.put("status", p.getStatus());
			map.put("total", p.getTotal());
			return map;
		}).collect(Collectors.toList());
	}

	public Map<String, Long> contadoresPorStatus() {
		Map<String, Long> contadores = new LinkedHashMap<>();
		List.of("CONFIRMADO", "SEPARANDO_PRODUTOS", "ENVIADO", "ENTREGUE", "CANCELADO")
				.forEach(s -> contadores.put(s, 0L));

		for (Object[] row : pedidoRepo.contagemPorStatusGeral()) { // preenche com os valores reais
			contadores.put((String) row[0], ((Number) row[1]).longValue());
		}
		return contadores;
	}

	public Map<String, Object> buscarDetalhe(Long pedidoId) {
		Pedido p = pedidoRepo.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

		Map<String, Object> map = new LinkedHashMap<>();
		map.put("pedido", p);
		map.put("nomeCliente", buscarNomeCliente(p.getClienteId()));
		map.put("emailCliente", buscarEmailCliente(p.getClienteId()));
		map.put("statusValidos", STATUS_VALIDOS);
		return map;
	}

	public void atualizarStatus(Long pedidoId, String novoStatus) {
		if (!STATUS_VALIDOS.contains(novoStatus)) {
			throw new RuntimeException("Status inválido: " + novoStatus);
		}

		Pedido pedido = pedidoRepo.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

		String statusAnterior = pedido.getStatus();
		pedido.setStatus(novoStatus);
		pedidoRepo.save(pedido);

		historicoRepo.save(new PedidoStatusHistorico(pedido, novoStatus));

		// envia e-mail se status mudou
		if (!statusAnterior.equals(novoStatus)) {
			String emailCliente = buscarEmailCliente(pedido.getClienteId());
			if (emailCliente != null) {
				emailService.enviarEmailStatusPedido(emailCliente, pedido.getId(), novoStatus);
			}
		}
	}

	// dados para dashboard
	public Map<String, Object> dadosDashboard(int ano) {
		Map<String, Object> dados = new LinkedHashMap<>();

		double[] vendasMes = new double[12];
		List<Object[]> resultado = pedidoRepo.vendasPorMes(ano);
		for (Object[] row : resultado) {
			int mes = ((Number) row[0]).intValue() - 1; // 0-indexed
			double total = ((Number) row[1]).doubleValue();
			vendasMes[mes] = total;
		}
		dados.put("vendasPorMes", vendasMes);

		// total do ano
		Double totalAno = pedidoRepo.totalVendasAno(ano);
		dados.put("totalVendasAno", totalAno != null ? totalAno : 0.0);

		// total de pedidos do ano
		Long totalPedidos = pedidoRepo.totalPedidosAno(ano);
		dados.put("totalPedidosAno", totalPedidos != null ? totalPedidos : 0L);

		// ticket médio
		double ticketMedio = (totalPedidos != null && totalPedidos > 0)
				? (totalAno != null ? totalAno : 0.0) / totalPedidos
				: 0.0;
		dados.put("ticketMedio", ticketMedio);

		// total de clientes
		dados.put("totalClientes", clienteRepo.count());

		// top 5 cervejas
		List<Map<String, Object>> topCervejas = new ArrayList<>();
		for (Object[] row : pedidoRepo.topCervejas(ano)) {
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("rotulo", row[0]);
			item.put("totalVendido", ((Number) row[1]).intValue());
			topCervejas.add(item);
		}
		dados.put("topCervejas", topCervejas);

		// contagem por status
		Map<String, Long> porStatus = new LinkedHashMap<>();
		for (Object[] row : pedidoRepo.contagemPorStatus(ano)) {
			porStatus.put((String) row[0], ((Number) row[1]).longValue());
		}
		dados.put("porStatus", porStatus);

		return dados;
	}

	private String buscarNomeCliente(int clienteId) {
		return clienteRepo.findById(clienteId).map(c -> c.getFirst_name() + " " + c.getLast_name())
				.orElse("Cliente #" + clienteId);
	}

	private String buscarEmailCliente(int clienteId) {
		return clienteRepo.findById(clienteId).map(Cliente::getEmail).orElse(null);
	}
}