package com.ecommerce.routeexpress.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.beans.factory.annotation.Value;
import java.util.Arrays;

import com.ecommerce.routeexpress.security.JwtAuthenticationFilter;

/**
 *
 * @author Daniel Arantes Telles
 */

@Configuration
public class SecurityConfig {

	@Value("${app.cors.origins}")
	private String corsOrigins;

	private final LoginSuccessHandler loginSuccessHandler;

	public SecurityConfig(LoginSuccessHandler loginSuccessHandler) {
		this.loginSuccessHandler = loginSuccessHandler;
	}

	// Bean para criptografia da senha
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/index.html", "/api/cervejas/**", "/api/carrinho/**",
								"/api/clientes/definir-senha", "/api/clientes/cadastro",
								"/api/clientes/confirmar-email", "/api/clientes/login", "/api/clientes/verificar-cpf",
								"/api/clientes/verificar-email", "/uploads/**", "/produtos/**", "/register",
								"/login_adm/**", "/css/**", "/api/clientes/recuperar-senha", "/api/clientes/nova-senha")
						.permitAll().requestMatchers("/adm/**").hasRole("MASTER").requestMatchers("/operator/**")
						.hasAnyRole("MASTER", "OPERATOR").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/adm/telaLogin").loginProcessingUrl("/adm")
						.successHandler(loginSuccessHandler) // ESSENCIAL !!!
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/adm/telaLogin") // Após o logout,
																									// direciona para a
																									// página inicial de
																									// login

						.permitAll());
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
			String path = request.getRequestURI();
			if (path.startsWith("/api/")) {
				response.setStatus(401);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("{\"erro\": \"Não autenticado. Faça login para continuar.\"}");
			} else {
				response.sendRedirect("/adm/telaLogin");
			}
		}));

		http.cors(cors -> cors.configurationSource(request -> {
			var config = new org.springframework.web.cors.CorsConfiguration();
			config.setAllowedOrigins(Arrays.asList(corsOrigins.split(",")));
			config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			config.setAllowedHeaders(List.of("*"));
			config.setAllowCredentials(false);
			return config;
		}));

		return http.build();

	}

}
