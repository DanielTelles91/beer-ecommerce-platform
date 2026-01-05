package com.ecommerce.routeexpress.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.routeexpress.services.CervejasRepositorio;
import com.ecommerce.routeexpress.services.ClientesRepositorio;
import com.ecommerce.routeexpress.services.database.ListaDeDesejoService;

@Controller
@RequestMapping("/lista")
public class ListaDeDesejosControle {

    private final ListaDeDesejoService service;

    @Autowired
    private ClientesRepositorio clienteRepo;

    @Autowired
    private CervejasRepositorio cervejaRepo;

    public ListaDeDesejosControle(ListaDeDesejoService service) {
        this.service = service;
    }

    // Exibir todos os itens da lista de desejos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("itens", service.listarTodos());
        return "listadedesejos/index"; 
    }

    // Tela para criar novo item
    @GetMapping("/novo")
    public String novoItem(Model model) {
        model.addAttribute("clientes", clienteRepo.findAll());
        model.addAttribute("cervejas", cervejaRepo.findAll());
        return "listadedesejos/CreateListaDeDesejo"; 
    }
 
    // Adicionar item à lista
    @PostMapping("/adicionar")
    public String adicionar(@RequestParam int clienteId, @RequestParam int cervejaId) {
        service.adicionar(clienteId, cervejaId);
        return "redirect:/lista";
    }

    // Remover item da lista
    @PostMapping("/remover")
    public String remover(@RequestParam int clienteId, @RequestParam int cervejaId) {
        service.remover(clienteId, cervejaId);
        return "redirect:/lista";
    }
}