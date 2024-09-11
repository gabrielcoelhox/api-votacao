package com.example.api_votacao.service;

import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.repository.SessaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessaoService.class);

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    @Lazy
    private VotacaoService votacaoService;

    public Sessao abrirSessao() {
        LOGGER.info("Abrindo nova sessão");
        verificarSessaoAberta();
        Sessao sessao = new Sessao();
        sessao.setInicio(LocalDateTime.now());
        sessao.setAberta(true);
        return sessaoRepository.save(sessao);
    }

    public Sessao fecharSessao(Long sessaoId) {
        LOGGER.info("Fechando sessão ID = {}", sessaoId);
        Sessao sessao = buscarSessaoPorId(sessaoId);
        verificarSessaoFechada(sessao);
        votacaoService.verificarQuantidadeVotos();

        sessao.setFim(LocalDateTime.now());
        sessao.setAberta(false);
        return sessaoRepository.save(sessao);
    }

    public Sessao buscarSessao(Long idSessao) {
        LOGGER.info("Buscando sessão ID = {}", idSessao);
        return buscarSessaoPorId(idSessao);
    }

    protected void verificarSessaoAberta() {
        if (sessaoRepository.existsByAbertaTrue()) {
            LOGGER.error("Já existe uma sessão aberta");
            throw new IllegalStateException("Já existe uma sessão aberta.");
        }
    }

    public Sessao buscarSessaoAberta() {
        LOGGER.info("Buscando sessão aberta");
        return sessaoRepository.findByAbertaTrue()
                .orElseThrow(() -> new IllegalStateException("Nenhuma sessão aberta encontrada"));
    }

    protected Sessao buscarSessaoPorId(Long sessaoId) {
        return sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> {
                    LOGGER.error("Sessão não encontrada: ID = {}", sessaoId);
                    return new IllegalArgumentException("Sessão não encontrada.");
                });
    }

    private void verificarSessaoFechada(Sessao sessao) {
        if (!sessao.isAberta()) {
            LOGGER.error("Sessão já está fechada: ID = {}", sessao.getId());
            throw new IllegalArgumentException("Sessão já está fechada.");
        }
    }

    protected void verificarSessaoEncerrada(Sessao sessao) {
        if (sessao.isAberta() || sessao.getFim() == null) {
            LOGGER.error("Sessão não encerrada ou não encontrada: ID = {}", sessao.getId());
            throw new IllegalArgumentException("Sessão não encerrada ou não encontrada.");
        }
    }
}
