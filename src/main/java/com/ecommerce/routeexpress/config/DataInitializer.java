package com.ecommerce.routeexpress.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ecommerce.routeexpress.models.Role;
import com.ecommerce.routeexpress.models.Usuario;
import com.ecommerce.routeexpress.services.UsuarioRepositorio;

/**
*
* @author Daniel A. Telles
*/

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner initAdmin(UsuarioRepositorio repo) {
		return args -> {
			if (repo.count() == 0) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

				Usuario admin = new Usuario();
				admin.setNome("Admin");
				admin.setSobrenome("Sistema");
				admin.setEmail("admin@admin.com");
				admin.setCpf("00000000000");
				admin.setSenha(encoder.encode("admin123"));
				admin.setRole(Role.MASTER);
				admin.setSenhaPadrao(true);

				repo.save(admin);

				System.out.println("✔ ADMIN padrão criado: admin@admin.com / admin123");
			}
		};
	}

}
