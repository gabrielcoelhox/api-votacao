package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Eleitor;
import com.example.api_votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eleitores")
public class EleitorController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    public List<Eleitor> listar() {
        return votacaoService.listarEleitores();
    }

    @PostMapping
    public Eleitor adicionar(@RequestBody Eleitor eleitor) {
        return votacaoService.adicionarEleitor(eleitor);
    }
}