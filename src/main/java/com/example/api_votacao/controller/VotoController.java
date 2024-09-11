package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Voto;
import com.example.api_votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
@Tag(name = "Votos", description = "Endpoints relacionados aos votos")
public class VotoController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    @Operation(summary = "Listar votos", description = "Retorna uma lista de todos os votos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de votos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<Voto> listarVotos() {
        return votacaoService.listarVotos();
    }

    @PostMapping("/eleitores/{eleitorId}/votar/{candidatoId}")
    @Operation(summary = "Adicionar voto", description = "Adiciona um novo voto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Voto votar(@PathVariable Long eleitorId, @PathVariable Long candidatoId) {
        return votacaoService.adicionarVoto(eleitorId, candidatoId);
    }
}