package com.ecommerce.routeexpress.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.routeexpress.services.UsuarioRepositorio;

/**
*
* @author Daniel Arantes Telles
*/

@Service
public class UsuarioService implements UserDetailsService {
	private final UsuarioRepositorio repo;

	public UsuarioService(UsuarioRepositorio repo) {
		this.repo = repo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}
}
