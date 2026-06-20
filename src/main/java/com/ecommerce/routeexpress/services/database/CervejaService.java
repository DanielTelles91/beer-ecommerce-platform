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
	private ImageStorageService imageStorageService;

	@Autowired
	private CervejariasRepositorio cervejariaRepo;

	@Autowired
	private CervejasRepositorio cervejasRepositorio;

	public Cerveja criaCerveja(CervejaDto cervejaDto) { // Creates a new Cerveja with images and saves it in the
														// database.

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
		return cervejasRepositorio.save(cerveja);
	}

	public void updateCerveja(int id, CervejaDto dto) { // Updates a beer based on the DTO, including images.

		Cerveja cerveja = cervejasRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));

		int cervejariaId = cerveja.getCervejaria().getId();

		if (cervejasRepositorio.existsByRotuloAndIdNot(dto.getRotulo(), id)) {
			throw new CervejaJaExisteException("Já existe um Rotulo com esse nome !");
		}

		// Updates images
		MultipartFile[] novasImagens = { dto.getImagem_1(), dto.getImagem_2(), dto.getImagem_3() };
		String[] imagensAntigas = { cerveja.getImagem_1(), cerveja.getImagem_2(), cerveja.getImagem_3() };
		String[] imagensSalvas = imageStorageService.atualizaImagensCerveja(cervejariaId, novasImagens, imagensAntigas);

		// Assign saved images
		cerveja.setImagem_1(imagensSalvas[0]);
		cerveja.setImagem_2(imagensSalvas[1]);
		cerveja.setImagem_3(imagensSalvas[2]);

		cerveja.setCor(dto.getCor());
		cerveja.setDescricao(dto.getDescricao());
		cerveja.setFamilia_e_estilo(dto.getFamilia_e_estilo());
		cerveja.setRotulo(dto.getRotulo());
		cerveja.setSabor(dto.getSabor());
		cerveja.setTemperatura(dto.getTemperatura());
		cerveja.setTeor(dto.getTeor());
		cerveja.setVolume(dto.getVolume());

		cervejasRepositorio.save(cerveja);
	}

	public void deleteCervejaById(int id) { // Deletes a Cerveja by its ID, including its images.
		Cerveja cerveja = cervejasRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Cerveja not found"));

		// Delete only the images related to this Cerveja
		imageStorageService.apagaImagensCerveja(cerveja);

		// Delete the Cerveja from the database
		cervejasRepositorio.delete(cerveja);
	}

	/**
	 * Finds a Cerveja by ID. Throws RuntimeException if not found.
	 */
	public Cerveja findById(int id) {
		return cervejasRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Cerveja not found"));
	}

	public CervejaDto mapToDto(Cerveja cerveja) { // Maps a Cerveja entity to a CervejaDto.
		CervejaDto dto = new CervejaDto();
		dto.setCor(cerveja.getCor());
		dto.setDescricao(cerveja.getDescricao());
		dto.setFamilia_e_estilo(cerveja.getFamilia_e_estilo());
		dto.setRotulo(cerveja.getRotulo());
		dto.setSabor(cerveja.getSabor());
		dto.setTemperatura(cerveja.getTemperatura());
		dto.setTeor(cerveja.getTeor());
		dto.setVolume(cerveja.getVolume());

		dto.setImagem_1(null);
		dto.setImagem_2(null);
		dto.setImagem_3(null);
		return dto;
	}

}
