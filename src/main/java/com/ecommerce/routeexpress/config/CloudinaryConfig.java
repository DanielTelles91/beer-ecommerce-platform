package com.ecommerce.routeexpress.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cloudinary.Cloudinary;

/**
 *
 * @author Daniel Arantes Telles
 */

@Configuration
@Profile("prod")
public class CloudinaryConfig {

	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	@Value("${cloudinary.api-key}")
	private String apiKey;

	@Value("${cloudinary.api-secret}")
	private String apiSecret;

	@Bean
	public Cloudinary cloudinary() {
		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		return new Cloudinary(config);
	}
}