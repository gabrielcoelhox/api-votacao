package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Cargo;
import com.example.api_votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    public List<Cargo> listar() {
        return votacaoService.listarCargos();
    }

    @PostMapping
    public Cargo adicionar(@RequestBody Cargo cargo) {
        return votacaoService.adicionarCargo(cargo);
    }

    @PutMapping("/{id}")
    public Cargo atualizar(@PathVariable Long id, @RequestBody Cargo cargo) {
        cargo.setId(id);
        return votacaoService.adicionarCargo(cargo);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        votacaoService.excluirCargo(id);
    }
}