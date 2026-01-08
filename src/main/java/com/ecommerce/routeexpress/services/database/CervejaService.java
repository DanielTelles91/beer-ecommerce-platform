package com.ecommerce.routeexpress.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.routeexpress.dto.CervejaDto;
import com.ecommerce.routeexpress.exceptions.CervejaJaExisteException;
import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.models.Cervejaria;
import com.ecommerce.routeexpress.services.CervejariasRepositorio;
import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.storage.ImageStorageService;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service

public class CervejaService {

	@Autowired
	private CervejasRepositorio repo;

	@Autowired
	private ImageStorageService imageStorageService;

	@Autowired
	private CervejariasRepositorio cervejariaRepo;
	
	@Autowired
	private CervejasRepositorio cervejasRepositorio;

	/**
	 * Deletes a Cerveja by its ID, including its images.
	 */
	public void deleteCervejaById(int id) {
		Cerveja cerveja = repo.findById(id).orElseThrow(() -> new RuntimeException("Cerveja not found"));

		// Delete only the images related to this Cerveja
		imageStorageService.apagaImagensCerveja(cerveja);

		// Delete the Cerveja from the database
		repo.delete(cerveja);
	}

	/**
	 * Creates a new Cerveja with images and saves it in the database.
	 */
	public Cerveja criaCerveja(CervejaDto cervejaDto) {
		int cervejariaId = cervejaDto.getCervejariaId();

		 if (cervejasRepositorio.existsByRotuloIgnoreCase(cervejaDto.getRotulo())) {
		        throw new CervejaJaExisteException("Já existe um Rótulo com esse nome !");
		    }
		
		// Save images via ImageStorageService
		String[] imagensSalvas = imageStorageService.salvaImagensCerveja(cervejariaId, cervejaDto.getImagem_1(),
				cervejaDto.getImagem_2(), cervejaDto.getImagem_3());

		// Build Cerveja entity
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

		// Link Cerveja to Cervejaria
		Cervejaria cervejaria = cervejariaRepo.findById(cervejariaId)
				.orElseThrow(() -> new RuntimeException("Cervejaria not found"));
		cerveja.setCervejaria(cervejaria);

		// Save in database
		return repo.save(cerveja);
	}

	/**
	 * Finds a Cerveja by ID. Throws RuntimeException if not found.
	 */
	public Cerveja findById(int id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Cerveja not found"));
	}

	/**
	 * Maps a Cerveja entity to a CervejaDto.
	 */
	public CervejaDto mapToDto(Cerveja cerveja) {
		CervejaDto dto = new CervejaDto();
		dto.setCor(cerveja.getCor());
		dto.setDescricao(cerveja.getDescricao());
		dto.setFamilia_e_estilo(cerveja.getFamilia_e_estilo());
		dto.setPreco(cerveja.getPreco());
		dto.setRotulo(cerveja.getRotulo());
		dto.setSabor(cerveja.getSabor());
		dto.setTemperatura(cerveja.getTemperatura());
		dto.setTeor(cerveja.getTeor());
		dto.setVolume(cerveja.getVolume());
		// você pode adicionar imagens se quiser mostrar no edit
		dto.setImagem_1(null);
		dto.setImagem_2(null);
		dto.setImagem_3(null);
		return dto;
	}

	/**
	 * Atualiza uma cerveja com base no DTO, incluindo imagens.
	 */
	public void atualizaCerveja(int id, CervejaDto dto) {

		Cerveja cerveja = repo.findById(id).orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));

		int cervejariaId = cerveja.getCervejaria().getId();


		
		if (cervejasRepositorio.existsByRotuloAndIdNot(dto.getRotulo(), id)) {
			throw new CervejaJaExisteException("Já existe um Rotulo com esse nome !");
		}
		
		
		// Atualiza imagens
		MultipartFile[] novasImagens = { dto.getImagem_1(), dto.getImagem_2(), dto.getImagem_3() };
		String[] imagensAntigas = { cerveja.getImagem_1(), cerveja.getImagem_2(), cerveja.getImagem_3() };
		String[] imagensSalvas = imageStorageService.atualizaImagensCerveja(cervejariaId, novasImagens, imagensAntigas);

		// Atribui as imagens salvas
		cerveja.setImagem_1(imagensSalvas[0]);
		cerveja.setImagem_2(imagensSalvas[1]);
		cerveja.setImagem_3(imagensSalvas[2]);

		// Atualiza os outros campos
		cerveja.setCor(dto.getCor());
		cerveja.setDescricao(dto.getDescricao());
		cerveja.setFamilia_e_estilo(dto.getFamilia_e_estilo());
		cerveja.setPreco(dto.getPreco());
		cerveja.setRotulo(dto.getRotulo());
		cerveja.setSabor(dto.getSabor());
		cerveja.setTemperatura(dto.getTemperatura());
		cerveja.setTeor(dto.getTeor());
		cerveja.setVolume(dto.getVolume());

		repo.save(cerveja);
	}

}
