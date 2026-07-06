package com.ecommerce.routeexpress.controllers.cliente;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cloudinary.Cloudinary;

import java.io.IOException;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
@Profile("prod")
public class ImageRedirectController {

	@Autowired
	private Cloudinary cloudinary;

	@GetMapping("/uploads/images/{cervejariaId}/{nomeArquivo}")
	public void redirect(@PathVariable int cervejariaId, @PathVariable String nomeArquivo, HttpServletResponse response)
			throws IOException {

		String semExtensao = nomeArquivo.contains(".") ? nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'))
				: nomeArquivo;

		String url = cloudinary.url().secure(true)
				.generate("routeexpress/cervejas/" + cervejariaId + "/" + semExtensao);

		response.sendRedirect(url);
	}
}
