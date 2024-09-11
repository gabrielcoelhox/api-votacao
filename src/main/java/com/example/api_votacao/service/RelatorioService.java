package com.example.api_votacao.service;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.entity.Voto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioService.class);

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private VotacaoService votacaoService;

    public String gerarBoletimUrna(Long idSessao) {
        LOGGER.info("Gerando boletim de urna para a sessão ID = {}", idSessao);
        Sessao sessao = sessaoService.buscarSessaoPorId(idSessao);
        sessaoService.verificarSessaoEncerrada(sessao);

        List<Voto> votos = votacaoService.buscarVotosPorSessao(idSessao);
        Map<Candidato, Long> votosPorCandidato = votos.stream()
                .collect(Collectors.groupingBy(Voto::getCandidato, Collectors.counting()));

        StringBuilder boletim = new StringBuilder();
        boletim.append(String.format("%-40s\n", "Boletim de Urna"));
        boletim.append(String.format("%-40s\n", "--------------------"));
        votosPorCandidato.forEach((candidato, count) -> {
            boletim.append(String.format("%-30s %10d\n", candidato.getNome(), count));
        });

        return boletim.toString();
    }

}
