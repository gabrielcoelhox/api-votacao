package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Eleitor;
import com.example.api_votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eleitores")
@Tag(name = "Eleitores", description = "Endpoints relacionados aos eleitores")
public class EleitorController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    @Operation(summary = "Listar eleitores", description = "Retorna uma lista de todos os eleitores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eleitores retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<Eleitor> listar() {
        return votacaoService.listarEleitores();
    }

    @PostMapping
    @Operation(summary = "Adicionar eleitor", description = "Adiciona um novo eleitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Eleitor adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Eleitor adicionar(@RequestBody Eleitor eleitor) {
        return votacaoService.adicionarEleitor(eleitor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar eleitor", description = "Atualiza um eleitor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleitor atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Eleitor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Eleitor atualizar(@PathVariable Long id, @RequestBody Eleitor eleitor) {
        eleitor.setId(id);
        return votacaoService.adicionarEleitor(eleitor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir eleitor", description = "Exclui um eleitor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Eleitor excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Eleitor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public void excluir(@PathVariable Long id) {
        votacaoService.excluirEleitor(id);
    }
}