package com.ecommerce.routeexpress.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.routeexpress.models.Carrinho;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface CarrinhoRepositorio extends JpaRepository<Carrinho, Long> {
	Optional<Carrinho> findBySessionId(String sessionId);

	List<Carrinho> findByDataAtualizacaoBefore(LocalDateTime limite);

	Optional<Carrinho> findByClienteId(int clienteId);
}
