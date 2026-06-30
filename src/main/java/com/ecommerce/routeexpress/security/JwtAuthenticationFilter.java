package com.ecommerce.routeexpress.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniel Arantes Telles
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			try {
				int clienteId = jwtUtil.extrairClienteId(token);

				var authentication = new UsernamePasswordAuthenticationToken(clienteId, null,
						List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")));

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (JwtException e) {
				// token inválido/expirado: simplesmente não autentica, segue sem usuário logado
			}
		}

		chain.doFilter(request, response);
	}
}
