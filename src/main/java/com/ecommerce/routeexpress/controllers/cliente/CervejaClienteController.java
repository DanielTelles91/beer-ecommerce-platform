package com.ecommerce.routeexpress.controllers.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.routeexpress.models.Cerveja;
import com.ecommerce.routeexpress.services.CervejasRepositorio;

@RestController
@RequestMapping("/api/cervejas")
@CrossOrigin(origins = "http://localhost:4200")
public class CervejaClienteController {

    @Autowired
    private CervejasRepositorio repo;

    
    @GetMapping
    public List<Cerveja> listar() {
        return repo.findAll();
    }

    
    @GetMapping("/{id}")
    public Cerveja buscarPorId(@PathVariable int id) {
        return repo.findById(id)
                   .orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));
    }
}
