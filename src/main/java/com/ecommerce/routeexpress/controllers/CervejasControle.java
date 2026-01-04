package com.ecommerce.routeexpress.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

		// salva arquivo imagem. Ok funciona porém precisa melhorar a lógica!

		MultipartFile image1 = cervejaDto.getImagem_1();
		MultipartFile image2 = cervejaDto.getImagem_2();
		MultipartFile image3 = cervejaDto.getImagem_3();
		Date createdAt = new Date();
		String storageFileName1 = createdAt.getTime() + "_" + image1.getOriginalFilename();
		String storageFileName2 = createdAt.getTime() + "_" + image2.getOriginalFilename();
		String storageFileName3 = createdAt.getTime() + "_" + image3.getOriginalFilename();

		try {
			String uploadDir = "public/uploads/images/";
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			try (InputStream inputStream = image1.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName1), StandardCopyOption.REPLACE_EXISTING);
			}

			try (InputStream inputStream = image2.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName2), StandardCopyOption.REPLACE_EXISTING);
			}

			try (InputStream inputStream = image3.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName3), StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
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
		cerveja.setImagem_1(storageFileName1);
		cerveja.setImagem_2(storageFileName2);
		cerveja.setImagem_3(storageFileName3);

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
	public String updateCerveja(Model model, @RequestParam int id, @Valid @ModelAttribute CervejaDto cervejaDto,
			BindingResult result) {

		try {
			Cerveja cerveja = repo.findById(id).get();
			model.addAttribute("cerveja", cerveja);

			if (result.hasErrors()) {
				return "cervejas/EditCerveja";
			}

			//

			if (!cervejaDto.getImagem_1().isEmpty()) {
				// delete old pic
				String uploadDir = "public/uploads/images/";
				Path oldImagePath = Paths.get(uploadDir + cerveja.getImagem_1());

				try {
					Files.delete(oldImagePath);
				} catch (Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}

				// salva o novo arquivo
				MultipartFile image = cervejaDto.getImagem_1();
				Date createdAt = new Date();
				String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
				}

				cerveja.setImagem_1(storageFileName);

			}
			//
			
			if (!cervejaDto.getImagem_2().isEmpty()) {
				// delete old pic
				String uploadDir = "public/uploads/images/";
				Path oldImagePath = Paths.get(uploadDir + cerveja.getImagem_2());

				try {
					Files.delete(oldImagePath);
				} catch (Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}

				// salva o novo arquivo
				MultipartFile image = cervejaDto.getImagem_2();
				Date createdAt = new Date();
				String storageFileName2 = createdAt.getTime() + "_" + image.getOriginalFilename();

				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName2),
							StandardCopyOption.REPLACE_EXISTING);
				}

				cerveja.setImagem_2(storageFileName2);

			}
			
			if (!cervejaDto.getImagem_3().isEmpty()) {
				// delete old pic
				String uploadDir = "public/uploads/images/";
				Path oldImagePath = Paths.get(uploadDir + cerveja.getImagem_3());

				try {
					Files.delete(oldImagePath);
				} catch (Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}

				// salva o novo arquivo
				MultipartFile image = cervejaDto.getImagem_3();
				Date createdAt = new Date();
				String storageFileName3 = createdAt.getTime() + "_" + image.getOriginalFilename();

				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName3),
							StandardCopyOption.REPLACE_EXISTING);
				}

				cerveja.setImagem_3(storageFileName3);

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
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/cervejas";
		}

		return "redirect:/cervejas";

	}

	@GetMapping("/delete")
	public String deleteCerveja(@RequestParam int id) {

		try {
			Cerveja cerveja = repo.findById(id).get();

			// Apaga o arquivo do diretório
			Path imagepath1 = Paths.get("public/uploads/images/" + cerveja.getImagem_1());
			Path imagepath2 = Paths.get("public/uploads/images/" + cerveja.getImagem_2());
			Path imagepath3 = Paths.get("public/uploads/images/" + cerveja.getImagem_3());

			try {
				Files.delete(imagepath1);
				Files.delete(imagepath2);
				Files.delete(imagepath3);
			} catch (Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}

			repo.delete(cerveja);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/cervejas";
		}

		return "redirect:/cervejas";
	}
}
