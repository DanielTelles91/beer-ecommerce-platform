package com.ecommerce.routeexpress.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
*
* @author Daniel A. Telles
*/

@Entity
@Table (name = "cervejaria") 

public class Cervejaria {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String cervejaria; 
	private String pais;
	
	
	
	@OneToMany(mappedBy = "cervejaria",
		       cascade = CascadeType.ALL,
		       orphanRemoval = true
		      )
    private List<Cerveja> cervejas = new ArrayList<>();



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getCervejaria() {
		return cervejaria;
	}



	public void setCervejaria(String cervejaria) {
		this.cervejaria = cervejaria;
	}



	public String getPais() {
		return pais;
	}



	public void setPais(String pais) {
		this.pais = pais;
	}
	

}
