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

import java.time.LocalDateTime;
import java.util.*;

@DisplayName("Testes do VotacaoService")
public class VotacaoServiceTest {

    @InjectMocks
    private VotacaoService votacaoService;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private EleitorRepository eleitorRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoRepository sessaoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Adicionar entidades")
    class AddEntitiesTests {

        @Test
        @DisplayName("Então adiciona um candidato com sucesso")
        public void adicionarCandidatoTest() {
            Candidato candidato = new Candidato();
            candidato.setNome("Candidato 1");

            when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

            Candidato result = votacaoService.adicionarCandidato(candidato);

            assertEquals("Candidato 1", result.getNome());
            verify(candidatoRepository, times(1)).save(candidato);
        }

        @Test
        @DisplayName("Então adiciona um eleitor com sucesso")
        public void adicionarEleitorTest() {
            Eleitor eleitor = new Eleitor();
            eleitor.setNome("Eleitor 1");

            when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

            Eleitor result = votacaoService.adicionarEleitor(eleitor);

            assertEquals("Eleitor 1", result.getNome());
            verify(eleitorRepository, times(1)).save(eleitor);
        }

        @Test
        @DisplayName("Então adiciona um voto com sucesso")
        public void adicionarVotoTest() {
            Candidato candidato = new Candidato();
            Eleitor eleitor = new Eleitor();
            Voto voto = new Voto();
            voto.setCandidato(candidato);
            voto.setEleitor(eleitor);

            when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidato));
            when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
            when(votoRepository.save(any(Voto.class))).thenReturn(voto);

            Voto result = votacaoService.adicionarVoto(1L, 1L);

            assertNotNull(result);
            verify(votoRepository, times(1)).save(any(Voto.class));
        }
    }

    @Nested
    @DisplayName("Listando entidades")
    class ListEntitiesTests {

        @Test
        @DisplayName("Então lista todos os candidatos com sucesso")
        public void listarCandidatosTest() {
            List<Candidato> candidatos = Arrays.asList(new Candidato(), new Candidato());

            when(candidatoRepository.findAll()).thenReturn(candidatos);

            List<Candidato> result = votacaoService.listarCandidatos();

            assertEquals(2, result.size());
            verify(candidatoRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Então lista todos os eleitores com sucesso")
        public void listarEleitoresTest() {
            List<Eleitor> eleitores = Arrays.asList(new Eleitor(), new Eleitor());

            when(eleitorRepository.findAll()).thenReturn(eleitores);

            List<Eleitor> result = votacaoService.listarEleitores();

            assertEquals(2, result.size());
            verify(eleitorRepository, times(1)).findAll();
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
    }

    @Nested
    @DisplayName("Gerenciando sessões")
    class ManageSessionsTests {

        @Test
        @DisplayName("Então abre uma sessão com sucesso")
        public void abrirSessaoTest() {
            Sessao sessao = new Sessao();
            sessao.setAberta(true);

            when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

            Sessao result = votacaoService.abrirSessao();

            assertTrue(result.isAberta());
            verify(sessaoRepository, times(1)).save(any(Sessao.class));
        }

        @Test
        @DisplayName("Então fecha uma sessão com sucesso")
        public void fecharSessaoTest() {
            Sessao sessao = new Sessao();
            sessao.setAberta(true);

            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.of(sessao));
            when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

            Sessao result = votacaoService.fecharSessao(1L);

            assertFalse(result.isAberta());
            verify(sessaoRepository, times(1)).save(any(Sessao.class));
        }

        @Test
        @DisplayName("Então busca uma sessão com sucesso")
        public void buscarSessaoTest() {
            Sessao sessao = new Sessao();

            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.of(sessao));

            Sessao result = votacaoService.buscarSessao(1L);

            assertNotNull(result);
            verify(sessaoRepository, times(1)).findById(anyLong());
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

            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.of(sessao));
            when(votoRepository.findBySessaoId(anyLong())).thenReturn(votos);

            String boletim = votacaoService.gerarBoletimUrna(1L);

            assertTrue(boletim.contains("Candidato 1"));
            verify(sessaoRepository, times(1)).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("Excluindo entidades")
    class DeleteEntitiesTests {

        @Test
        @DisplayName("Então exclui um candidato com sucesso")
        public void excluirCandidatoTest() {
            votacaoService.excluirCandidato(1L);

            verify(candidatoRepository, times(1)).deleteById(anyLong());
        }

        @Test
        @DisplayName("Então exclui um eleitor com sucesso")
        public void excluirEleitorTest() {
            votacaoService.excluirEleitor(1L);

            verify(eleitorRepository, times(1)).deleteById(anyLong());
        }
    }
}