package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Voto;
import com.example.api_votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
public class VotoController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    public List<Voto> listar() {
        return votacaoService.listarVotos();
    }

    @PostMapping("/eleitores/{eleitorId}/votar/{candidatoId}")
    public Voto votar(@PathVariable Long eleitorId, @PathVariable Long candidatoId) {
        return votacaoService.adicionarVoto(eleitorId, candidatoId);
    }
}