package com.example.api_votacao.controller;

import com.example.api_votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorio")
@Tag(name = "Relatórios", description = "Endpoints relacionados aos relatórios")
public class RelatorioController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping("/boletim-urna/{idSessao}")
    @Operation(summary = "Gerar boletim de urna", description = "Gera o boletim de urna para uma sessão específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Boletim de urna gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public String gerarBoletimUrna(@PathVariable Long idSessao) {
        return votacaoService.gerarBoletimUrna(idSessao);
    }
}