package com.ecommerce.routeexpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author Daniel Arantes Telles
 */

@SpringBootApplication
@EnableScheduling
public class RouteexpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouteexpressApplication.class, args);
	}

}
