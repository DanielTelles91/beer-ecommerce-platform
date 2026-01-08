package com.ecommerce.routeexpress.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ecommerce.routeexpress.models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
*
* @author Daniel Arantes Telles
*/

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		Usuario usuario = (Usuario) authentication.getPrincipal();

		if (usuario.isSenhaPadrao()) {
			// Se estiver usando senha padrão, redireciona para trocar senha
			response.sendRedirect("/adm/mudar-senha");
		} else {
			// Caso contrário, redireciona para home
			response.sendRedirect("/adm/telaInicialAdm");
		}
	}
}
