package com.ecommerce.routeexpress.services.storage;

import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.routeexpress.models.Cerveja;

/**
 *
 * @author Daniel Arantes Telles
 */

public interface IImageStorageService {
	void apagaPastaImagem(int cervejariaId);

	void apagaImagensCerveja(Cerveja cerveja);

	String[] salvaImagensCerveja(int cervejariaId, MultipartFile image1, MultipartFile image2, MultipartFile image3);

	String[] atualizaImagensCerveja(int cervejariaId, MultipartFile[] novasImagens, String[] imagensAntigas);
}
