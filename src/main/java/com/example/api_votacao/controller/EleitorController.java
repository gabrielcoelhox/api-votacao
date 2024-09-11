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

    @PutMapping("/{id}")
    public Eleitor atualizar(@PathVariable Long id, @RequestBody Eleitor eleitor) {
        eleitor.setId(id);
        return votacaoService.adicionarEleitor(eleitor);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        votacaoService.excluirEleitor(id);
    }
}