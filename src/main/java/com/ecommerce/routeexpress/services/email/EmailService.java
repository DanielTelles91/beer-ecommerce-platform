package com.ecommerce.routeexpress.services.email;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.routeexpress.dto.ItemPedidoDto;
import com.ecommerce.routeexpress.dto.PedidoDto;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
public class EmailService {

	@Value("${brevo.api-key}")
	private String apiKey;

	@Value("${app.mail.from}")
	private String emailRemetente;

	@Value("${app.mail.from-name}")
	private String nomeRemetente;

	@Value("${app.frontend.url}")
	private String frontendUrl;

	private final RestTemplate restTemplate = new RestTemplate();
	private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

	public void enviarEmailDefinirSenha(String destinatario, String token) {
		String link = frontendUrl + "/definir-senha?token=" + token;
		String corpo = "Olá!\n\nSeu cadastro na Route Express foi criado por nossa equipe.\n\n"
				+ "Para confirmar seu e-mail e definir sua senha de acesso, clique no link abaixo:\n" + link
				+ "\n\nSe você não esperava este e-mail, ignore esta mensagem.";
		enviar(destinatario, "Defina sua senha - Route Express", corpo);
	}

	public void enviarEmailConfirmacaoCadastro(String destinatario, String token) {
		String link = frontendUrl + "/confirmar-email?token=" + token;
		String corpo = "Olá!\n\nObrigado por se cadastrar na Route Express.\n\n"
				+ "Confirme seu e-mail clicando no link abaixo:\n" + link
				+ "\n\nSe você não fez esse cadastro, ignore este e-mail.";
		enviar(destinatario, "Confirme seu cadastro - Route Express", corpo);
	}

	public void enviarEmailRecuperacaoSenha(String destinatario, String token) {
		String link = frontendUrl + "/nova-senha?token=" + token;
		String corpo = "Olá!\n\nRecebemos uma solicitação para redefinir sua senha.\n\n"
				+ "Clique no link abaixo para criar uma nova senha:\n" + link
				+ "\n\nSe você não solicitou isso, ignore este e-mail.";
		enviar(destinatario, "Recuperação de senha - Route Express", corpo);
	}

	public void enviarEmailConfirmacaoPedido(String destinatario, PedidoDto pedido) {
		StringBuilder corpo = new StringBuilder();
		corpo.append("Olá!\n\n");
		corpo.append("Seu pedido #").append(pedido.getId()).append(" foi confirmado!\n\n");
		corpo.append("═══════════════════════════════\n");
		corpo.append("ITENS DO PEDIDO\n");
		corpo.append("═══════════════════════════════\n\n");

		for (ItemPedidoDto item : pedido.getItens()) {
			corpo.append("• ").append(item.getRotulo()).append("\n");
			corpo.append("  ").append(item.getQuantidade()).append("x ");
			corpo.append("R$ ").append(String.format("%.2f", item.getPrecoUnitario()));
			corpo.append(" = R$ ").append(String.format("%.2f", item.getSubtotal()));
			corpo.append("\n\n");
		}

		corpo.append("═══════════════════════════════\n");
		corpo.append("TOTAL: R$ ").append(String.format("%.2f", pedido.getTotal())).append("\n");
		corpo.append("═══════════════════════════════\n\n");
		corpo.append("Endereço de entrega:\n");
		corpo.append(pedido.getEnderecoCompleto()).append("\n\n");
		corpo.append("Obrigado pela sua compra na Route Express!\n");

		enviar(destinatario, "Pedido #" + pedido.getId() + " confirmado - Route Express", corpo.toString());
	}

	public void enviarEmailStatusPedido(String destinatario, Long pedidoId, String novoStatus) {
		String statusFormatado = switch (novoStatus) {
		case "SEPARANDO_PRODUTOS" -> "Separando produtos";
		case "ENVIADO" -> "Enviado para transportadora";
		case "ENTREGUE" -> "Entregue com sucesso";
		case "CANCELADO" -> "Cancelado";
		default -> novoStatus;
		};

		String corpo = "Olá!\n\n" + "O status do seu pedido #" + pedidoId + " foi atualizado:\n\n" + " "
				+ statusFormatado + "\n\n" + "Acesse sua conta para mais detalhes.\n\n" + "Route Express ";

		enviar(destinatario, "Atualização do pedido #" + pedidoId + " - Route Express", corpo);
	}

	private void enviar(String destinatario, String assunto, String corpo) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("api-key", apiKey);

			Map<String, Object> payload = Map.of("sender", Map.of("email", emailRemetente, "name", nomeRemetente), "to",
					List.of(Map.of("email", destinatario)), "subject", assunto, "textContent", corpo);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

			// debug
			var response = restTemplate.postForEntity(BREVO_URL, request, String.class);
			System.out.println("Brevo status: " + response.getStatusCode());
			System.out.println("Brevo response: " + response.getBody());

		} catch (Exception e) {
			System.err.println("Erro ao enviar e-mail via Brevo: " + e.getMessage());
			e.printStackTrace(); 
		}
	}
}