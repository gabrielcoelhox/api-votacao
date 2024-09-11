package com.example.api_votacao.service;

import com.example.api_votacao.entity.*;
import com.example.api_votacao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VotacaoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    public Candidato adicionarCandidato(Candidato candidato) {
        if (candidatoRepository.existsByNome(candidato.getNome())) {
            throw new IllegalArgumentException("Candidato já existe.");
        }
        return candidatoRepository.save(candidato);
    }

    public Eleitor adicionarEleitor(Eleitor eleitor) {
        if (eleitorRepository.existsByNome(eleitor.getNome())) {
            throw new IllegalArgumentException("Eleitor já existe.");
        }
        return eleitorRepository.save(eleitor);
    }

    public List<Candidato> listarCandidatos() {
        return candidatoRepository.findAll();
    }

    public List<Eleitor> listarEleitores() {
        return eleitorRepository.findAll();
    }

    public List<Voto> listarVotos() {
        return votoRepository.findAll();
    }

    public Voto adicionarVoto(Long eleitorId, Long candidatoId) {

        if (!sessaoRepository.existsByAbertaTrue()) {
            throw new IllegalStateException("Sessão não está aberta.");
        }
        if (votoRepository.existsByEleitorId(eleitorId)) {
            throw new IllegalArgumentException("Eleitor já votou.");
        }

        Optional<Candidato> candidato = candidatoRepository.findById(candidatoId);
        Optional<Eleitor> eleitor = eleitorRepository.findById(eleitorId);
        if (candidato.isEmpty() || eleitor.isEmpty()) {
            throw new IllegalArgumentException("Candidato ou Eleitor não encontrado.");
        }

        Voto voto = new Voto();
        voto.setCandidato(candidato.get());
        voto.setEleitor(eleitor.get());
        return votoRepository.save(voto);
    }

    public Sessao abrirSessao() {

        if (sessaoRepository.existsByAbertaTrue()) {
            throw new IllegalStateException("Já existe uma sessão aberta.");
        }

        Sessao sessao = new Sessao();
        sessao.setInicio(LocalDateTime.now());
        sessao.setAberta(true);
        return sessaoRepository.save(sessao);
    }

    public Sessao fecharSessao(Long sessaoId) {

        Optional<Sessao> sessao = sessaoRepository.findById(sessaoId);
        if (sessao.isEmpty() || !sessao.get().isAberta()) {
            throw new IllegalArgumentException("Sessão não encontrada ou já está fechada.");
        }
        long votos = votoRepository.count();
        if (votos == 1) {
            votoRepository.deleteAll();
        }
        sessao.get().setFim(LocalDateTime.now());
        sessao.get().setAberta(false);
        return sessaoRepository.save(sessao.get());
    }

    public Sessao buscarSessao(Long idSessao) {
        return sessaoRepository.findById(idSessao)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada."));
    }

    public List<Voto> buscarVotosPorSessao(Long idSessao) {
        return votoRepository.findBySessaoId(idSessao);
    }
}