package com.example.api_votacao.service;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.entity.Cargo;
import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.entity.Voto;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Mock
    private CargoService cargoService;

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
            Sessao sessao = criarSessao();
            List<Voto> votos = criarListaVotos();
            Cargo cargo = criarCargo();

            mockDependencias(sessao, votos, cargo);

            String boletim = relatorioService.gerarBoletimUrna(1L);

            verificarBoletim(boletim);
            verificarBuscarSessaoPorId();
            verificarVerificarSessaoEncerrada();
            verificarBuscarVotosPorSessao();
            verificarBuscarCargoPorId();
        }

        private Sessao criarSessao() {
            Sessao sessao = new Sessao();
            sessao.setId(1L);
            sessao.setAberta(false);
            sessao.setFim(LocalDateTime.now());
            return sessao;
        }

        private List<Voto> criarListaVotos() {
            Candidato candidato = new Candidato();
            candidato.setNome("Candidato 1");

            Voto voto = new Voto();
            voto.setCandidato(candidato);

            return Arrays.asList(voto, voto);
        }

        private Cargo criarCargo() {
            Cargo cargo = new Cargo();
            cargo.setId(1L);
            cargo.setNome("Presidente");
            return cargo;
        }

        private void mockDependencias(Sessao sessao, List<Voto> votos, Cargo cargo) {
            when(sessaoService.buscarSessaoPorId(anyLong())).thenReturn(sessao);
            doNothing().when(sessaoService).verificarSessaoEncerrada(any(Sessao.class));
            when(votacaoService.buscarVotosPorSessao(anyLong())).thenReturn(votos);
            when(cargoService.buscarCargoPorId(anyLong())).thenReturn(cargo);
        }

        private void verificarBoletim(String boletim) {
            assertTrue(boletim.contains("Candidato 1"));
            assertTrue(boletim.contains("Presidente"));
        }

        private void verificarBuscarSessaoPorId() {
            verify(sessaoService, times(1)).buscarSessaoPorId(anyLong());
        }

        private void verificarVerificarSessaoEncerrada() {
            verify(sessaoService, times(1)).verificarSessaoEncerrada(any(Sessao.class));
        }

        private void verificarBuscarVotosPorSessao() {
            verify(votacaoService, times(1)).buscarVotosPorSessao(anyLong());
        }

        private void verificarBuscarCargoPorId() {
            verify(cargoService, times(1)).buscarCargoPorId(anyLong());
        }
    }
}