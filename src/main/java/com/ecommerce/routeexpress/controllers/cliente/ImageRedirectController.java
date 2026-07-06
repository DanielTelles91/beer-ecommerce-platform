package com.ecommerce.routeexpress.controllers.cliente;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;

/**
 *
 * @author Daniel Arantes Telles
 */

@Controller
@Profile("prod")
public class ImageRedirectController {

	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	@GetMapping("/uploads/images/{cervejariaId}/{nomeArquivo}")
	public void redirect(@PathVariable int cervejariaId, @PathVariable String nomeArquivo, HttpServletResponse response)
			throws IOException {
		String url = "https://res.cloudinary.com/" + cloudName + "/image/upload/routeexpress/cervejas/" + cervejariaId
				+ "/" + nomeArquivo;
		response.sendRedirect(url);
	}
}
