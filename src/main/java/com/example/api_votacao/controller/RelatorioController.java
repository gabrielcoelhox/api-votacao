package com.example.api_votacao.controller;

import com.example.api_votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping("/boletim-urna/{idSessao}")
    public String gerarBoletimUrna(@PathVariable Long idSessao) {
        return votacaoService.gerarBoletimUrna(idSessao);
    }
}