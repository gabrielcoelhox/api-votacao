package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessao")
public class SessaoController {

    @Autowired
    private VotacaoService votacaoService;

    @PostMapping("/abrir")
    public Sessao abrirSessao() {
        return votacaoService.abrirSessao();
    }

    @PatchMapping("/fechar/{id}")
    public Sessao fecharSessao(@PathVariable Long id) {
        return votacaoService.fecharSessao(id);
    }
}