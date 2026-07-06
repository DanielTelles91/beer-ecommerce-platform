package com.ecommerce.routeexpress.services.storage;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.routeexpress.models.Cerveja;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
@Profile("prod")
public class CloudinaryImageStorageService implements IImageStorageService {

	@Autowired
	private Cloudinary cloudinary;

	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	private String pasta(int cervejariaId) {
		return "routeexpress/cervejas/" + cervejariaId;
	}

	@Override
	public void apagaPastaImagem(int cervejariaId) {
		try {
			cloudinary.api().deleteResourcesByPrefix(pasta(cervejariaId) + "/", ObjectUtils.emptyMap());
		} catch (Exception e) {
			System.out.println("Erro ao apagar pasta Cloudinary: " + e.getMessage());
		}
	}

	@Override
	public void apagaImagensCerveja(Cerveja cerveja) {
		int cervejariaId = cerveja.getCervejaria().getId();
		apagarArquivo(cervejariaId, cerveja.getImagem_1());
		apagarArquivo(cervejariaId, cerveja.getImagem_2());
		apagarArquivo(cervejariaId, cerveja.getImagem_3());
	}

	private void apagarArquivo(int cervejariaId, String nomeArquivo) {

		if (nomeArquivo == null || nomeArquivo.isBlank())
			return;

		try {

			String semExtensao = nomeArquivo.contains(".") ? nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'))
					: nomeArquivo;

			cloudinary.uploader().destroy(pasta(cervejariaId) + "/" + semExtensao, ObjectUtils.emptyMap());

		} catch (Exception e) {

			System.out.println("Erro ao apagar arquivo Cloudinary: " + nomeArquivo);

		}

	}

	@Override
	public String[] salvaImagensCerveja(int cervejariaId, MultipartFile image1, MultipartFile image2,
			MultipartFile image3) {
		String[] nomesSalvos = new String[3];
		MultipartFile[] imagens = { image1, image2, image3 };

		for (int i = 0; i < imagens.length; i++) {
			if (imagens[i] != null && !imagens[i].isEmpty()) {
				nomesSalvos[i] = uploadImagem(imagens[i], cervejariaId);
			}
		}

		return nomesSalvos;
	}

	@Override
	public String[] atualizaImagensCerveja(int cervejariaId, MultipartFile[] novasImagens, String[] imagensAntigas) {
		String[] imagensSalvas = new String[novasImagens.length];

		for (int i = 0; i < novasImagens.length; i++) {
			if (novasImagens[i] != null && !novasImagens[i].isEmpty()) {
				apagarArquivo(cervejariaId, imagensAntigas[i]);
				imagensSalvas[i] = uploadImagem(novasImagens[i], cervejariaId);
			} else {
				imagensSalvas[i] = imagensAntigas[i];
			}
		}

		return imagensSalvas;
	}

	private String uploadImagem(MultipartFile img, int cervejariaId) { //

		try {

			String nomeOriginal = img.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");

			String semExtensao = nomeOriginal.contains(".") ? nomeOriginal.substring(0, nomeOriginal.lastIndexOf('.'))
					: nomeOriginal;

			String publicId = System.currentTimeMillis() + "_" + semExtensao;

			Map<?, ?> resultado = cloudinary.uploader().upload(img.getBytes(),
					ObjectUtils.asMap("folder", pasta(cervejariaId), "public_id", publicId));

			// pega somente o nome da imagem
			String idCompleto = (String) resultado.get("public_id");

			return idCompleto.replace(pasta(cervejariaId) + "/", "") + "." + resultado.get("format");

		} catch (Exception e) {

			System.out.println("Erro ao fazer upload Cloudinary: " + e.getMessage());
			return null;

		}

	}
}
