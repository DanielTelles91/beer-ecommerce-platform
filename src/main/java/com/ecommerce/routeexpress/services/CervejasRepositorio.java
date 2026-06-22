package com.ecommerce.routeexpress.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.routeexpress.models.Cerveja;

/**
 *
 * @author Daniel Arantes Telles
 */
public interface CervejasRepositorio extends JpaRepository<Cerveja, Integer> {

	boolean existsByRotuloIgnoreCase(String rotulo);

	boolean existsByRotuloAndIdNot(String Rotulo, int id);

	@Query("""
				SELECT c
				FROM Cerveja c
				JOIN c.estoques e
				WHERE e.disponibilidade = true
				AND (:pais IS NULL OR c.cervejaria.pais = :pais)
			""")
	Page<Cerveja> findDisponiveis(@Param("pais") String pais, Pageable pageable);

	@Query("""
				SELECT c FROM Cerveja c
				JOIN c.estoques e
				WHERE e.disponibilidade = true
				AND LOWER(c.rotulo) LIKE LOWER(CONCAT('%', :nome, '%'))
				AND (:pais IS NULL OR c.cervejaria.pais = :pais)
			""")
	Page<Cerveja> buscarPorNome(@Param("nome") String nome, @Param("pais") String pais, Pageable pageable);

	@Query("""
				SELECT DISTINCT c.cervejaria.pais
				FROM Cerveja c
				JOIN c.estoques e
				WHERE e.disponibilidade = true
				ORDER BY c.cervejaria.pais
			""")
	List<String> listarPaisesDisponiveis();

	@Query("""
			    SELECT c FROM Cerveja c
			    JOIN c.estoques e
			    WHERE e.disponibilidade = true
			    AND (:pais IS NULL OR c.cervejaria.pais = :pais)
			    ORDER BY e.preco ASC
			""")
	Page<Cerveja> findDisponiveisPrecoAsc(@Param("pais") String pais, Pageable pageable);

	@Query("""
			    SELECT c FROM Cerveja c
			    JOIN c.estoques e
			    WHERE e.disponibilidade = true
			    AND (:pais IS NULL OR c.cervejaria.pais = :pais)
			    ORDER BY e.preco DESC
			""")
	Page<Cerveja> findDisponiveisPrecoDesc(@Param("pais") String pais, Pageable pageable);

	@Query("""
			    SELECT c FROM Cerveja c
			    JOIN c.estoques e
			    WHERE e.disponibilidade = true
			    AND LOWER(c.rotulo) LIKE LOWER(CONCAT('%', :nome, '%'))
			    AND (:pais IS NULL OR c.cervejaria.pais = :pais)
			    ORDER BY e.preco ASC
			""")
	Page<Cerveja> buscarPorNomePrecoAsc(@Param("nome") String nome, @Param("pais") String pais, Pageable pageable);

	@Query("""
			    SELECT c FROM Cerveja c
			    JOIN c.estoques e
			    WHERE e.disponibilidade = true
			    AND LOWER(c.rotulo) LIKE LOWER(CONCAT('%', :nome, '%'))
			    AND (:pais IS NULL OR c.cervejaria.pais = :pais)
			    ORDER BY e.preco DESC
			""")
	Page<Cerveja> buscarPorNomePrecoDesc(@Param("nome") String nome, @Param("pais") String pais, Pageable pageable);

}