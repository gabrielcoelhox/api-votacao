package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.service.CandidatoService;
import com.example.api_votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidatos")
@Tag(name = "Candidatos", description = "Endpoints relacionados aos candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @GetMapping
    @Operation(summary = "Listar candidatos", description = "Retorna uma lista de todos os candidatos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de candidatos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<Candidato> listar() {
        return candidatoService.listarCandidatos();
    }

    @PostMapping
    @Operation(summary = "Adicionar candidato", description = "Adiciona um novo candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidato adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Candidato adicionar(@RequestBody Candidato candidato) {
        return candidatoService.adicionarCandidato(candidato);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar candidato", description = "Atualiza um candidato existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Candidato atualizar(@PathVariable Long id, @RequestBody Candidato candidato) {
        candidato.setId(id);
        return candidatoService.adicionarCandidato(candidato);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir candidato", description = "Exclui um candidato existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidato excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public void excluir(@PathVariable Long id) {
        candidatoService.excluirCandidato(id);
    }
}