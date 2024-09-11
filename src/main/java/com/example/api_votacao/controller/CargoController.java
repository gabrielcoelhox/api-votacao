package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Cargo;
import com.example.api_votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cargos")
@Tag(name = "Cargos", description = "Endpoints relacionados aos cargos")
public class CargoController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    @Operation(summary = "Listar cargos", description = "Retorna uma lista de todos os cargos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cargos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<Cargo> listar() {
        return votacaoService.listarCargos();
    }

    @PostMapping
    @Operation(summary = "Adicionar cargo", description = "Adiciona um novo cargo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cargo adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Cargo adicionar(@RequestBody Cargo cargo) {
        return votacaoService.adicionarCargo(cargo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cargo", description = "Atualiza um cargo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Cargo atualizar(@PathVariable Long id, @RequestBody Cargo cargo) {
        cargo.setId(id);
        return votacaoService.atualizarCargo(id, cargo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cargo", description = "Exclui um cargo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cargo excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public void excluir(@PathVariable Long id) {
        votacaoService.excluirCargo(id);
    }
}