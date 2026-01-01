package com.ecommerce.routeexpress.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.routeexpress.models.Product;
import com.ecommerce.routeexpress.models.ProductDto;
import com.ecommerce.routeexpress.services.ProdutosRepositorio;

@Controller
@RequestMapping("/products")  
public class ProdutosControle {
	
	@Autowired
	private ProdutosRepositorio repo;
	
	@GetMapping({"", "/"})
	public String showProductList(Model model) {
		List<Product> products = repo.findAll();
		model.addAttribute("products", products);
		return "products/index";
		
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto", productDto);
		return "products/CreateProduct";
	}

}
