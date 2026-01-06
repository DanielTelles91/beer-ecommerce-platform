package com.ecommerce.routeexpress.services;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.routeexpress.models.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);
}
