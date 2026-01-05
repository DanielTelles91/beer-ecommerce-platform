package com.ecommerce.routeexpress.services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path; 
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.routeexpress.models.Cerveja;

@Service
public class ImageStorageService {

	public void apagaPastaImagem(int id) {

		try {

			// pasta da cervejaria
			Path folderPath = Paths.get("public/uploads/images/" + id);

			// Apaga tudo dentro da pasta recursivamente
			if (Files.exists(folderPath)) {
				Files.walk(folderPath).sorted(Comparator.reverseOrder()) // apaga arquivos antes da pasta
						.forEach(path -> {
							try {
								Files.delete(path);
							} catch (Exception e) {
								System.out.println("Erro ao apagar: " + path + " -> " + e.getMessage());
							}
						});
			}

		} catch (IOException e) {
			System.out.println("Erro ao acessar pasta da cervejaria " + id);
		}

	}

	public void apagaImagensCerveja(Cerveja cerveja) {

		int cervejariaId = cerveja.getCervejaria().getId();
		String baseDir = "public/uploads/images/" + cervejariaId + "/";

		apagarArquivo(baseDir, cerveja.getImagem_1());
		apagarArquivo(baseDir, cerveja.getImagem_2());
		apagarArquivo(baseDir, cerveja.getImagem_3());
	}

	private void apagarArquivo(String baseDir, String nomeArquivo) {
		if (nomeArquivo == null || nomeArquivo.isBlank()) {
			return;
		}

		try {
			Path path = Paths.get(baseDir + nomeArquivo);
			Files.deleteIfExists(path);
		} catch (Exception e) {
			System.out.println("Erro ao apagar arquivo: " + nomeArquivo);
		}
	}

	public String[] salvaImagensCerveja(int cervejariaId, MultipartFile image1, MultipartFile image2,
			MultipartFile image3) {
		String[] nomesSalvos = new String[3];
		MultipartFile[] imagens = { image1, image2, image3 };

		String uploadDir = "public/uploads/images/" + cervejariaId + "/";
		Path uploadPath = Paths.get(uploadDir);

		try {
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			for (int i = 0; i < imagens.length; i++) {
				MultipartFile img = imagens[i];
				if (img != null && !img.isEmpty()) {
					String storageFileName = System.currentTimeMillis() + "_" + img.getOriginalFilename();
					try (InputStream inputStream = img.getInputStream()) {
						Files.copy(inputStream, uploadPath.resolve(storageFileName),
								StandardCopyOption.REPLACE_EXISTING);
					}
					nomesSalvos[i] = storageFileName;
				}
			}
		} catch (Exception ex) {
			System.out.println("Erro ao salvar imagens: " + ex.getMessage());
		}

		return nomesSalvos;
	}

	
	
	
	public String[] atualizaImagensCerveja(int cervejariaId, MultipartFile[] novasImagens, String[] imagensAntigas) {
	    String[] imagensSalvas = new String[novasImagens.length];

	    String uploadDir = "public/uploads/images/" + cervejariaId + "/";

	    for (int i = 0; i < novasImagens.length; i++) {
	        MultipartFile image = novasImagens[i];

	        if (image != null && !image.isEmpty()) {
	            // deleta a antiga
	            if (imagensAntigas[i] != null) {
	                Path oldImagePath = Paths.get(uploadDir + imagensAntigas[i]);
	                try { Files.deleteIfExists(oldImagePath); } catch (Exception e) { e.printStackTrace(); }
	            }

	            // salva a nova
	            String storageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
	            Path uploadPath = Paths.get(uploadDir);
	            try {
	                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
	                try (InputStream inputStream = image.getInputStream()) {
	                    Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

	            imagensSalvas[i] = storageFileName;
	        } else {
	            // mantém a antiga caso não tenha enviado nova
	            imagensSalvas[i] = imagensAntigas[i];
	        }
	    }

	    return imagensSalvas;
	}
	
	
}
