package com.example.api_votacao.controller;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.entity.Voto;
import com.example.api_votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping("/boletim-urna/{idSessao}")
    public String gerarBoletimUrna(@PathVariable Long idSessao) {
        Sessao sessao = votacaoService.buscarSessao(idSessao);
        validarSessaoEncerrada(sessao);
        Map<Candidato, Long> votos = contarVotosPorSessao(idSessao);
        return formatarBoletimUrna(votos);
    }

    private void validarSessaoEncerrada(Sessao sessao) {
        if (sessao.isAberta() || sessao.getFim() == null) {
            throw new IllegalArgumentException("Sessão não encerrada ou não encontrada.");
        }
    }

    private Map<Candidato, Long> contarVotosPorSessao(Long idSessao) {
        List<Voto> votos = votacaoService.buscarVotosPorSessao(idSessao);
        return votos.stream().collect(Collectors.groupingBy(Voto::getCandidato, Collectors.counting()));
    }

    private String formatarBoletimUrna(Map<Candidato, Long> votos) {
        StringBuilder boletim = new StringBuilder();
        boletim.append(String.format("%-40s\n", "Boletim de Urna"));
        boletim.append(String.format("%-40s\n", "--------------------"));
        votos.forEach((candidato, count) -> {
            boletim.append(String.format("%-30s %10d\n", candidato.getNome(), count));
        });
        return boletim.toString();
    }
}