package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessao")
@Tag(name = "Sessões", description = "Endpoints relacionados às sessões de votação")
public class SessaoController {

    @Autowired
    @Lazy
    private SessaoService sessaoService;

    @PostMapping("/abrir")
    @Operation(summary = "Abrir sessão", description = "Abre uma nova sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Sessao abrirSessao() {
        return sessaoService.abrirSessao();
    }

    @PatchMapping("/fechar/{id}")
    @Operation(summary = "Fechar sessão", description = "Fecha uma sessão de votação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão fechada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Sessao fecharSessao(@PathVariable Long id) {
        return sessaoService.fecharSessao(id);
    }
}