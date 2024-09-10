package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    public List<Candidato> listar() {
        return votacaoService.listarCandidatos();
    }

    @PostMapping
    public Candidato adicionar(@RequestBody Candidato candidato) {
        return votacaoService.adicionarCandidato(candidato);
    }
}