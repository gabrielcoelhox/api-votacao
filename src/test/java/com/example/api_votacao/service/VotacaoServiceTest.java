package com.example.api_votacao.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.api_votacao.entity.*;
import com.example.api_votacao.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

@DisplayName("Testes do VotacaoService")
public class VotacaoServiceTest {

    @InjectMocks
    private VotacaoService votacaoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private EleitorService eleitorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testes de adicionar voto")
    class AdicionarVotoTests {

        @Test
        @DisplayName("Deve adicionar um voto com sucesso")
        public void adicionarVotoComSucesso() {
            Candidato candidato = new Candidato();
            Eleitor eleitor = new Eleitor();
            Voto voto = new Voto();
            voto.setCandidato(candidato);
            voto.setEleitor(eleitor);

            when(candidatoService.buscarCandidatoPorId(anyLong())).thenReturn(candidato);
            when(eleitorService.buscarEleitorPorId(anyLong())).thenReturn(eleitor);
            when(votoRepository.save(any(Voto.class))).thenReturn(voto);
            doNothing().when(sessaoService).verificarSessaoAberta();

            Voto result = votacaoService.adicionarVoto(1L, 1L);

            assertNotNull(result);
            verify(votoRepository, times(1)).save(any(Voto.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar voto em sessão fechada")
        public void adicionarVotoEmSessaoFechada() {
            doThrow(new IllegalStateException("Nenhuma sessão aberta encontrada"))
                    .when(sessaoService).buscarSessaoAberta();

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                votacaoService.adicionarVoto(1L, 1L);
            });

            assertEquals("Nenhuma sessão aberta encontrada", exception.getMessage());
            verify(votoRepository, never()).save(any(Voto.class));
        }
    }

    @Nested
    @DisplayName("Testes de listar votos")
    class ListarVotosTests {

        @Test
        @DisplayName("Deve listar todos os votos com sucesso")
        public void listarVotosComSucesso() {
            List<Voto> votos = Arrays.asList(new Voto(), new Voto());

            when(votoRepository.findAll()).thenReturn(votos);

            List<Voto> result = votacaoService.listarVotos();

            assertEquals(2, result.size());
            verify(votoRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de buscar votos por sessão")
    class BuscarVotosPorSessaoTests {

        @Test
        @DisplayName("Deve buscar votos por sessão com sucesso")
        public void buscarVotosPorSessaoComSucesso() {
            List<Voto> votos = Arrays.asList(new Voto(), new Voto());

            when(votoRepository.findBySessaoId(anyLong())).thenReturn(votos);

            List<Voto> result = votacaoService.buscarVotosPorSessao(1L);

            assertEquals(2, result.size());
            verify(votoRepository, times(1)).findBySessaoId(anyLong());
        }
    }

    @Nested
    @DisplayName("Testes de verificar quantidade de votos")
    class VerificarQuantidadeVotosTests {

        @Test
        @DisplayName("Deve verificar quantidade de votos com sucesso")
        public void verificarQuantidadeVotosComSucesso() {
            when(votoRepository.count()).thenReturn(10L);

            long quantidade = votacaoService.verificarQuantidadeVotos();

            assertEquals(10L, quantidade);
            verify(votoRepository, times(1)).count();
        }
    }
}