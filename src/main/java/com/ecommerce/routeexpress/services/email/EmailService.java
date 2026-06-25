package com.ecommerce.routeexpress.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
		String corpo = "Olá!\n\nRecebemos uma solicitação para redefinir sua senha.\n\n"
				+ "Clique no link abaixo para criar uma nova senha:\n" + link
				+ "\n\nSe você não solicitou isso, ignore este e-mail.";

		enviar(destinatario, "Recuperação de senha - Route Express", corpo);
	}

	private void enviar(String destinatario, String assunto, String corpo) {
		SimpleMailMessage mensagem = new SimpleMailMessage();
		mensagem.setFrom("hello@demomailtrap.co");
		mensagem.setTo(destinatario);
		mensagem.setSubject(assunto);
		mensagem.setText(corpo);
		mailSender.send(mensagem);
	}
}
