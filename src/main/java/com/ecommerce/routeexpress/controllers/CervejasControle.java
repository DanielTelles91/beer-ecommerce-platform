package com.ecommerce.routeexpress.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.CervejaDto;
import com.ecommerce.routeexpress.models.Cervejaria;
import com.ecommerce.routeexpress.models.CervejariaDto;
import com.ecommerce.routeexpress.services.CervejariasRepositorio;
import com.ecommerce.routeexpress.services.CervejasRepositorio;

import jakarta.validation.Valid;

/**
*
* @author Daniel A. Telles
*/

@Controller
@RequestMapping("/cervejas")

public class CervejasControle {

	@Autowired
	private CervejasRepositorio repo;
	
	@Autowired
	private CervejariasRepositorio repo2;
	
	
	@GetMapping({"", "/"})
	public String showCervejaList(Model model) {
		List<Cerveja> cervejas = repo.findAll();
		model.addAttribute("cervejas", cervejas);
		return "cervejas/index"; // diretório cervejas/index		
	}
	
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		CervejaDto cervejaDto = new CervejaDto();
		model.addAttribute("cervejaDto", cervejaDto);
		model.addAttribute("cerveja", new Cerveja ());
		model.addAttribute("cervejarias", repo2.findAll());

		return "cervejas/CreateCerveja";
	}
	
	
	@PostMapping("/create")
	public String createCerveja(
		@Valid @ModelAttribute CervejaDto cervejaDto,
		BindingResult result, RedirectAttributes redirectAttributes
		) {
		
		if (result.hasErrors()) { // Caso algum campo não esteja preenchido, fica na página Createcliente
			return "cervejas/CreateCerveja";
			 
		}	
					
		Cerveja cerveja = new Cerveja();
	    cerveja.setCor(cervejaDto.getCor());
	    cerveja.setDescricao(cervejaDto.getDescricao());
	    cerveja.setFamilia_e_estilo(cervejaDto.getFamilia_e_estilo());
	    cerveja.setPreco(cervejaDto.getPreco());
	    cerveja.setRotulo(cervejaDto.getRotulo());
	    cerveja.setSabor(cervejaDto.getSabor());
	    cerveja.setTemperatura(cervejaDto.getTemperatura());
	    cerveja.setTeor(cervejaDto.getTeor());
	    cerveja.setVolume(cervejaDto.getVolume());
	 	
		
		int id = cervejaDto.getCervejariaId();
		Cervejaria cervejaria = repo2.findById(id).get();
		cerveja.setCervejaria(cervejaria); //<<< MUITO IMPORTANTE-
		
		repo.save(cerveja);  //Salva no BD
	
        return "redirect:/cervejas";
	}
	
	
	@GetMapping("/edit")
	public String showEditPage(
			Model model,
			@RequestParam int id
			) {
		
		try {
			Cerveja cerveja = repo.findById(id).get();
			model.addAttribute("cerveja", cerveja);
			
			CervejaDto cervejaDto = new CervejaDto();
			cervejaDto.setCor(cerveja.getCor());
			cervejaDto.setDescricao(cerveja.getDescricao());
			cervejaDto.setFamilia_e_estilo(cerveja.getFamilia_e_estilo());
			cervejaDto.setPreco(cerveja.getPreco());
			cervejaDto.setRotulo(cerveja.getRotulo());
			cervejaDto.setSabor(cerveja.getSabor());
			cervejaDto.setTemperatura(cerveja.getTemperatura());
			cervejaDto.setTeor(cerveja.getTeor());
			cervejaDto.setVolume(cerveja.getVolume());
		
			model.addAttribute("cervejaDto", cervejaDto);
			
		}
		catch(Exception ex) {
			System.out.println("Exception: "+ ex.getMessage());
			return "redirect:/cervejas";
		}	
		
		return "cervejas/EditCerveja";
	}
	
	@PostMapping("/edit")
	public String updateCerveja(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute CervejaDto cervejaDto,
			BindingResult result
			) {
		
		try {
			Cerveja cerveja = repo.findById(id).get();
			model.addAttribute("cerveja", cerveja);
			
			if (result.hasErrors()) {
				return "cervejas/EditCerveja";
			}
			cerveja.setCor(cervejaDto.getCor());
			cerveja.setDescricao(cervejaDto.getDescricao());
			cerveja.setFamilia_e_estilo(cervejaDto.getFamilia_e_estilo());
			cerveja.setPreco(cervejaDto.getPreco());
			cerveja.setRotulo(cervejaDto.getRotulo());
			cerveja.setSabor(cervejaDto.getSabor());
			cerveja.setTemperatura(cervejaDto.getTemperatura());
			cerveja.setTeor(cervejaDto.getTeor());
			cerveja.setVolume(cervejaDto.getVolume());
			
			repo.save(cerveja); // Salva no banco de dados
		}
		catch(Exception ex) {
			System.out.println("Exception: "+ ex.getMessage());
			return "redirect:/cervejas";
		}	
		
		return "redirect:/cervejas";
		
	}
	
	
	@GetMapping("/delete")
	public String deleteCerveja(
			@RequestParam int id
			) {
		
		try {
			Cerveja cerveja = repo.findById(id).get();
			
			repo.delete(cerveja);
			
		}
		catch(Exception ex) {
			System.out.println("Exception: "+ ex.getMessage());
			return "redirect:/cervejas";
		}
		
		return "redirect:/cervejas";
	}	
}
