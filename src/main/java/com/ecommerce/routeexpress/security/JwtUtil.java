package com.ecommerce.routeexpress.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 *
 * @author Daniel Arantes Telles
 */

@Component
public class JwtUtil {

	@Value("${app.jwt.secret}")
	private String secret;

	@Value("${app.jwt.expiration-ms}")
	private long expirationMs;

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String gerarToken(int clienteId, String email) {
		Date agora = new Date();
		Date expiracao = new Date(agora.getTime() + expirationMs);

		return Jwts.builder().subject(String.valueOf(clienteId)).claim("email", email).issuedAt(agora)
				.expiration(expiracao).signWith(getKey()).compact();
	}

	public Claims validarToken(String token) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
	}

	public int extrairClienteId(String token) {
		return Integer.parseInt(validarToken(token).getSubject());
	}
}
