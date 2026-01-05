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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.CervejaDto;
import com.ecommerce.routeexpress.models.Cervejaria;

import com.ecommerce.routeexpress.services.CervejariasRepositorio;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.storage.ImageStorageService;

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

	@Autowired
	private ImageStorageService imageStorageService;

	@GetMapping({ "", "/" })
	public String showCervejaList(Model model) {
		List<Cerveja> cervejas = repo.findAll();
		model.addAttribute("cervejas", cervejas);
		return "cervejas/index"; // diretório cervejas/index
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		CervejaDto cervejaDto = new CervejaDto();
		model.addAttribute("cervejaDto", cervejaDto);
		model.addAttribute("cerveja", new Cerveja());
		model.addAttribute("cervejarias", repo2.findAll());

		return "cervejas/CreateCerveja";
	}

	@PostMapping("/create")
	public String createCerveja(@Valid @ModelAttribute CervejaDto cervejaDto, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) { // Caso algum campo não esteja preenchido, fica na página Createcliente
			return "cervejas/CreateCerveja";
		}

		int cervejariaId = cervejaDto.getCervejariaId();

		// Salva imagens via service
		String[] imagensSalvas = imageStorageService.salvaImagensCerveja(cervejariaId, cervejaDto.getImagem_1(),
				cervejaDto.getImagem_2(), cervejaDto.getImagem_3());

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
		cerveja.setImagem_1(imagensSalvas[0]);
		cerveja.setImagem_2(imagensSalvas[1]);
		cerveja.setImagem_3(imagensSalvas[2]);

		int id = cervejaDto.getCervejariaId();
		Cervejaria cervejaria = repo2.findById(id).get();
		cerveja.setCervejaria(cervejaria); // <<< MUITO IMPORTANTE-

		repo.save(cerveja); // Salva no BD

		return "redirect:/cervejas";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

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

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/cervejas";
		}

		return "cervejas/EditCerveja";
	}

	@PostMapping("/edit")
	public String updateCerveja(@RequestParam int id, @Valid @ModelAttribute CervejaDto cervejaDto,
			BindingResult result) {

		if (result.hasErrors()) {
			return "cervejas/EditCerveja";
		}

		Cerveja cerveja = repo.findById(id).orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));
		int cervejariaId = cerveja.getCervejaria().getId();

		// Atualiza imagens usando service
		MultipartFile[] novasImagens = { cervejaDto.getImagem_1(), cervejaDto.getImagem_2(), cervejaDto.getImagem_3() };
		String[] imagensAntigas = { cerveja.getImagem_1(), cerveja.getImagem_2(), cerveja.getImagem_3() };
		String[] imagensSalvas = imageStorageService.atualizaImagensCerveja(cervejariaId, novasImagens, imagensAntigas);

		// Atribui as imagens salvas
		for (int i = 0; i < imagensSalvas.length; i++) {
			switch (i) {
			case 0 -> cerveja.setImagem_1(imagensSalvas[i]);
			case 1 -> cerveja.setImagem_2(imagensSalvas[i]);
			case 2 -> cerveja.setImagem_3(imagensSalvas[i]);
			}
		}

		// Atualiza os outros campos
		cerveja.setCor(cervejaDto.getCor());
		cerveja.setDescricao(cervejaDto.getDescricao());
		cerveja.setFamilia_e_estilo(cervejaDto.getFamilia_e_estilo());
		cerveja.setPreco(cervejaDto.getPreco());
		cerveja.setRotulo(cervejaDto.getRotulo());
		cerveja.setSabor(cervejaDto.getSabor());
		cerveja.setTemperatura(cervejaDto.getTemperatura());
		cerveja.setTeor(cervejaDto.getTeor());
		cerveja.setVolume(cervejaDto.getVolume());

		repo.save(cerveja);

		return "redirect:/cervejas";
	}

	@GetMapping("/delete")
	public String deleteCerveja(@RequestParam int id) {

		try {
			Cerveja cerveja = repo.findById(id).get();

			imageStorageService.apagaImagensCerveja(cerveja); // Apaga apenas imagens de cerveja.

			// Apaga do banco
			repo.delete(cerveja);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/cervejas";
		}

		return "redirect:/cervejas";

	}
}
