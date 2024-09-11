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
    @DisplayName("Testes do VotacaoService")
    class VotacaoServiceTests {

        @Test
        @DisplayName("Então adiciona um voto com sucesso")
        public void adicionarVotoTest() {
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
        @DisplayName("Então lista todos os votos com sucesso")
        public void listarVotosTest() {
            List<Voto> votos = Arrays.asList(new Voto(), new Voto());

            when(votoRepository.findAll()).thenReturn(votos);

            List<Voto> result = votacaoService.listarVotos();

            assertEquals(2, result.size());
            verify(votoRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Então busca votos por sessão com sucesso")
        public void buscarVotosPorSessaoTest() {
            List<Voto> votos = Arrays.asList(new Voto(), new Voto());

            when(votoRepository.findBySessaoId(anyLong())).thenReturn(votos);

            List<Voto> result = votacaoService.buscarVotosPorSessao(1L);

            assertEquals(2, result.size());
            verify(votoRepository, times(1)).findBySessaoId(anyLong());
        }
    }
}