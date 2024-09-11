package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidatos")
@Tag(name = "Candidatos", description = "Endpoints relacionados aos candidatos")
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

    @PutMapping("/{id}")
    public Candidato atualizar(@PathVariable Long id, @RequestBody Candidato candidato) {
        candidato.setId(id);
        return votacaoService.adicionarCandidato(candidato);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        votacaoService.excluirCandidato(id);
    }
}