package com.example.api_votacao.service;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.entity.Voto;
import com.example.api_votacao.repository.SessaoRepository;
import com.example.api_votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Testes do RelatorioService")
public class RelatorioServiceTest {

    @InjectMocks
    private RelatorioService relatorioService;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private VotacaoService votacaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testes do RelatorioService")
    class RelatorioServiceTests {

        @Test
        @DisplayName("Então gera boletim de urna com sucesso")
        public void gerarBoletimUrnaTest() {
            Sessao sessao = new Sessao();
            sessao.setId(1L);
            sessao.setAberta(false);
            sessao.setFim(LocalDateTime.now());

            Candidato candidato = new Candidato();
            candidato.setNome("Candidato 1");

            Voto voto = new Voto();
            voto.setCandidato(candidato);

            List<Voto> votos = Arrays.asList(voto, voto);

            when(sessaoService.buscarSessaoPorId(anyLong())).thenReturn(sessao);
            doNothing().when(sessaoService).verificarSessaoEncerrada(any(Sessao.class));
            when(votacaoService.buscarVotosPorSessao(anyLong())).thenReturn(votos);

            String boletim = relatorioService.gerarBoletimUrna(1L);

            assertTrue(boletim.contains("Candidato 1"));
            verify(sessaoService, times(1)).buscarSessaoPorId(anyLong());
            verify(sessaoService, times(1)).verificarSessaoEncerrada(any(Sessao.class));
            verify(votacaoService, times(1)).buscarVotosPorSessao(anyLong());
        }
    }
}