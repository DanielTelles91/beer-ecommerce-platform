package com.ecommerce.routeexpress.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.dto.ItemPedidoDto;
import com.ecommerce.routeexpress.dto.PedidoDto;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.frontend.url}")
	private String frontendUrl;

	public void enviarEmailConfirmacaoCadastro(String destinatario, String token) {
		String link = frontendUrl + "/confirmar-email?token=" + token;
		String corpo = "Olá!\n\nObrigado por se cadastrar na Route Express.\n\n"
				+ "Confirme seu e-mail clicando no link abaixo:\n" + link
				+ "\n\nSe você não fez esse cadastro, ignore este e-mail.";

		enviar(destinatario, "Confirme seu cadastro - Route Express", corpo);
	}

	public void enviarEmailRecuperacaoSenha(String destinatario, String token) {
		String link = frontendUrl + "/nova-senha?token=" + token;
		String corpo = "Olá!\n\n" + "Recebemos uma solicitação para redefinir sua senha.\n\n"
				+ "Clique no link abaixo para criar uma nova senha:\n" + link + "\n\nEste link expira em 1 hora.\n\n"
				+ "Se você não solicitou isso, ignore este e-mail.\n\n" + "Route Express";
		enviar(destinatario, "Recuperação de senha - Route Express", corpo);
	}

	private void enviar(String destinatario, String assunto, String corpo) {
		SimpleMailMessage mensagem = new SimpleMailMessage();
		mensagem.setFrom("route.express.beer@gmail.com");
		mensagem.setTo(destinatario);
		mensagem.setSubject(assunto);
		mensagem.setText(corpo);
		mailSender.send(mensagem);
	}

	public void enviarEmailDefinirSenha(String destinatario, String token) {
		String link = frontendUrl + "/definir-senha?token=" + token;
		String corpo = "Olá!\n\nSeu cadastro na Route Express foi criado por nossa equipe.\n\n"
				+ "Para confirmar seu e-mail e definir sua senha de acesso, clique no link abaixo:\n" + link
				+ "\n\nSe você não esperava este e-mail, ignore esta mensagem.";

		enviar(destinatario, "Defina sua senha - Route Express", corpo);
	}

	public void enviarEmailConfirmacaoPedido(String destinatario, PedidoDto pedido) {
		StringBuilder corpo = new StringBuilder();
		corpo.append("Olá!\n\n");
		corpo.append("Seu pedido #").append(pedido.getId()).append(" foi confirmado com sucesso!\n\n");
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
				+ statusFormatado + "\n\n" + "Acesse sua conta para mais detalhes.\n\n" + "Route Express";

		enviar(destinatario, "Atualização do pedido #" + pedidoId + " - Route Express", corpo);
	}

}
