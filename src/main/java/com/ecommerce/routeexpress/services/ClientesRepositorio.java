package com.ecommerce.routeexpress.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.routeexpress.models.Cliente;
import java.util.Optional;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface ClientesRepositorio extends JpaRepository<Cliente, Integer> {
	Cliente findByCpf(String cpf); // permite buscar cliente pelo CPF

	boolean existsByCpfIgnoreCase(String cpf);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByCpfIgnoreCaseAndIdNot(String cpf, int id);

	boolean existsByEmailIgnoreCaseAndIdNot(String email, int id);
	
	Optional<Cliente> findByTokenConfirmacao(String token);

}
