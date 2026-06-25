package com.ecommerce.routeexpress.controllers.teste;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.routeexpress.services.email.EmailService;

@RestController
public class TesteEmailController {

	@Autowired
	private EmailService emailService;

	@GetMapping("/teste-email")
	public String testar(@RequestParam String email) {
		emailService.enviarEmailConfirmacaoCadastro(email, "TOKEN-DE-TESTE-123");
		return "E-mail enviado (verifique sua caixa no Mailtrap)";
	}
}