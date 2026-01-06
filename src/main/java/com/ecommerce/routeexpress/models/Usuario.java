package com.ecommerce.routeexpress.models;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

/**
 *
 * @author Daniel A. Telles
 */

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String nome;
	private String sobrenome;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String cpf;

	@Column(nullable = false)
	private String senha;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private boolean senhaPadrao = true;
	
	 @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return Collections.singleton(() -> "ROLE_" + this.getRole().name());
	    }

	    @Override
	    public String getPassword() {
	        return this.senha;
	    }

	    @Override
	    public String getUsername() {
	        return this.email; // login por email
	    }

	    @Override
	    public boolean isAccountNonExpired() { return true; }

	    @Override
	    public boolean isAccountNonLocked() { return true; }

	    @Override
	    public boolean isCredentialsNonExpired() { return true; }

	    @Override
	    public boolean isEnabled() { return true; }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isSenhaPadrao() {
		return senhaPadrao;
	}

	public void setSenhaPadrao(boolean senhaPadrao) {
		this.senhaPadrao = senhaPadrao;
	}
	

}
