package com.example.api_votacao.service;

import com.example.api_votacao.entity.*;
import com.example.api_votacao.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotacaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoService.class);

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    @Lazy
    private EleitorService eleitorService;

    @Autowired
    @Lazy
    private SessaoService sessaoService;

    public List<Voto> listarVotos() {
        LOGGER.info("Listando todos os votos");
        return votoRepository.findAll();
    }

    public Voto adicionarVoto(Long eleitorId, Long candidatoId) {
        LOGGER.info("Adicionando voto: Eleitor ID = {}, Candidato ID = {}", eleitorId, candidatoId);
        Sessao sessao = sessaoService.buscarSessaoAberta();
        verificarEleitorJaVotou(eleitorId);

        Candidato candidato = candidatoService.buscarCandidatoPorId(candidatoId);
        Eleitor eleitor = eleitorService.buscarEleitorPorId(eleitorId);

        Voto voto = new Voto();
        voto.setCandidato(candidato);
        voto.setEleitor(eleitor);
        voto.setSessao(sessao);
        return votoRepository.save(voto);
    }

    public List<Voto> buscarVotosPorSessao(Long idSessao) {
        LOGGER.info("Buscando votos para a sessão ID = {}", idSessao);
        return votoRepository.findBySessaoId(idSessao);
    }

    private void verificarEleitorJaVotou(Long eleitorId) {
        if (votoRepository.existsByEleitorId(eleitorId)) {
            LOGGER.error("Eleitor já votou: ID = {}", eleitorId);
            throw new IllegalArgumentException("O Eleitor já votou.");
        }
    }

    protected long verificarQuantidadeVotos() {
        long votos = votoRepository.count();
        if (votos == 1) {
            LOGGER.error("Sessão não pode ser encerrada com 1 voto");
            throw new IllegalArgumentException("Sessão não pode ser encerrada com 1 voto.");
        }
        return votos;
    }

    protected void verificarEleitorComVotos(Long eleitorId) {
        if (votoRepository.existsByEleitorId(eleitorId)) {
            LOGGER.error("Eleitor não pode ser excluído, pois possui votos: ID = {}", eleitorId);
            throw new IllegalArgumentException("Eleitor não pode ser excluído, pois possui votos.");
        }
    }
}