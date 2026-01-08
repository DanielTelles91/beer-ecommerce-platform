package com.ecommerce.routeexpress.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
*
* @author Daniel Arantes Telles
*/

@Configuration
public class SecurityConfig {

	
	  private final LoginSuccessHandler loginSuccessHandler;

	    public SecurityConfig(LoginSuccessHandler loginSuccessHandler) {
	        this.loginSuccessHandler = loginSuccessHandler;
	    }
	    
	    
	    
	// Bean para criptografia de senha
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/", "/index.html", "/clientes/**", "/produtos/**", "/register", "/login_adm/**").permitAll()
	            .requestMatchers("/adm/**").hasRole("MASTER")
	            .requestMatchers("/operator/**").hasAnyRole("MASTER", "OPERATOR")
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/adm/telaLogin")
	            .loginProcessingUrl("/adm")
	            .successHandler(loginSuccessHandler) //ESSENCIAL !!!
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/adm/telaLogin") // Após o logout, direciona para a página inicial de Login
	            .permitAll()
	        );

	    return http.build();
	}


}
