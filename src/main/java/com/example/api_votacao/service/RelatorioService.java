package com.example.api_votacao.service;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.entity.Cargo;
import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.entity.Voto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioService.class);

    @Autowired
    @Lazy
    private RelatorioService relatorioService;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private VotacaoService votacaoService;

    @Autowired
    private CargoService cargoService;

    public String gerarBoletimUrna(Long idSessao) {
        LOGGER.info("Gerando boletim de urna para a sessão ID = {}", idSessao);
        Sessao sessao = sessaoService.buscarSessaoPorId(idSessao);
        sessaoService.verificarSessaoEncerrada(sessao);

        List<Voto> votos = votacaoService.buscarVotosPorSessao(idSessao);
        Map<Candidato, Long> votosPorCandidato = agruparVotosPorCandidato(votos);

        long totalVotos = calcularTotalVotos(votosPorCandidato);
        Candidato vencedor = determinarVencedor(votosPorCandidato);

        Cargo cargo = cargoService.buscarCargoPorId(idSessao);

        return formatarBoletim(sessao, votosPorCandidato, totalVotos, vencedor, cargo);
    }

    private Map<Candidato, Long> agruparVotosPorCandidato(List<Voto> votos) {
        return votos.stream()
                .collect(Collectors.groupingBy(Voto::getCandidato, Collectors.counting()));
    }

    private long calcularTotalVotos(Map<Candidato, Long> votosPorCandidato) {
        return votosPorCandidato.values().stream().mapToLong(Long::longValue).sum();
    }

    private Candidato determinarVencedor(Map<Candidato, Long> votosPorCandidato) {
        return votosPorCandidato.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String formatarBoletim(Sessao sessao, Map<Candidato, Long> votosPorCandidato, long totalVotos, Candidato vencedor, Cargo cargo) {
        StringBuilder boletim = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataRelatorio = LocalDateTime.now().format(formatter);

        boletim.append("----------------------------------------\n");
        boletim.append(String.format("Data relatório: %-20s\n", dataRelatorio));
        boletim.append(String.format("Cargo: %s\n", cargo.getNome()));
        boletim.append(String.format("%-30s %10s\n", "Candidatos", "Votos"));

        votosPorCandidato.forEach((candidato, count) -> {
            boletim.append(String.format("%-30s %10d\n", candidato.getNome(), count));
        });

        boletim.append(String.format("%-30s %10d\n", "Total de votos", totalVotos));
        if (vencedor != null) {
            boletim.append(String.format("Vencedor: %-30s\n", vencedor.getNome()));
        }
        boletim.append("----------------------------------------\n");

        return boletim.toString();
    }
}