package com.example.api_votacao.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.api_votacao.entity.*;
import com.example.api_votacao.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

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

    @Mock
    private CargoRepository cargoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void adicionarCandidatoTest() {
        Candidato candidato = new Candidato();
        candidato.setNome("Candidato 1");

        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        Candidato result = votacaoService.adicionarCandidato(candidato);

        assertEquals("Candidato 1", result.getNome());
        verify(candidatoRepository, times(1)).save(candidato);
    }

    @Test
    public void adicionarEleitorTest() {
        Eleitor eleitor = new Eleitor();
        eleitor.setNome("Eleitor 1");

        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor result = votacaoService.adicionarEleitor(eleitor);

        assertEquals("Eleitor 1", result.getNome());
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    public void listarCandidatosTest() {
        List<Candidato> candidatos = Arrays.asList(new Candidato(), new Candidato());

        when(candidatoRepository.findAll()).thenReturn(candidatos);

        List<Candidato> result = votacaoService.listarCandidatos();

        assertEquals(2, result.size());
        verify(candidatoRepository, times(1)).findAll();
    }

    @Test
    public void listarEleitoresTest() {
        List<Eleitor> eleitores = Arrays.asList(new Eleitor(), new Eleitor());

        when(eleitorRepository.findAll()).thenReturn(eleitores);

        List<Eleitor> result = votacaoService.listarEleitores();

        assertEquals(2, result.size());
        verify(eleitorRepository, times(1)).findAll();
    }

    @Test
    public void listarVotosTest() {
        List<Voto> votos = Arrays.asList(new Voto(), new Voto());

        when(votoRepository.findAll()).thenReturn(votos);

        List<Voto> result = votacaoService.listarVotos();

        assertEquals(2, result.size());
        verify(votoRepository, times(1)).findAll();
    }

    @Test
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

    @Test
    public void abrirSessaoTest() {
        Sessao sessao = new Sessao();
        sessao.setAberta(true);

        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        Sessao result = votacaoService.abrirSessao();

        assertTrue(result.isAberta());
        verify(sessaoRepository, times(1)).save(any(Sessao.class));
    }

    @Test
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
    public void buscarSessaoTest() {
        Sessao sessao = new Sessao();

        when(sessaoRepository.findById(anyLong())).thenReturn(Optional.of(sessao));

        Sessao result = votacaoService.buscarSessao(1L);

        assertNotNull(result);
        verify(sessaoRepository, times(1)).findById(anyLong());
    }

    @Test
    public void buscarVotosPorSessaoTest() {
        List<Voto> votos = Arrays.asList(new Voto(), new Voto());

        when(votoRepository.findBySessaoId(anyLong())).thenReturn(votos);

        List<Voto> result = votacaoService.buscarVotosPorSessao(1L);

        assertEquals(2, result.size());
        verify(votoRepository, times(1)).findBySessaoId(anyLong());
    }

    @Test
    public void excluirCandidatoTest() {
        votacaoService.excluirCandidato(1L);

        verify(candidatoRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void excluirEleitorTest() {
        votacaoService.excluirEleitor(1L);

        verify(eleitorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void adicionarCargoTest() {
        Cargo cargo = new Cargo();
        cargo.setDescricao("Cargo 1");

        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        Cargo result = votacaoService.adicionarCargo(cargo);

        assertEquals("Cargo 1", result.getDescricao());
        verify(cargoRepository, times(1)).save(cargo);
    }

    @Test
    public void atualizarCargoTest() {
        Cargo cargo = new Cargo();
        cargo.setId(1L);
        cargo.setDescricao("Descricao Atualizada");

        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        Cargo atualizado = votacaoService.atualizarCargo(1L, cargo);

        assertEquals("Descricao Atualizada", atualizado.getDescricao());
        verify(cargoRepository, times(1)).findById(1L);
        verify(cargoRepository, times(1)).save(cargo);
    }

    @Test
    public void listarCargosTest() {
        List<Cargo> cargos = Arrays.asList(new Cargo(), new Cargo());

        when(cargoRepository.findAll()).thenReturn(cargos);

        List<Cargo> result = votacaoService.listarCargos();

        assertEquals(2, result.size());
        verify(cargoRepository, times(1)).findAll();
    }

    @Test
    public void excluirCargoTest() {
        votacaoService.excluirCargo(1L);

        verify(cargoRepository, times(1)).deleteById(anyLong());
    }

    @Test
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
        verify(votoRepository, times(1)).findBySessaoId(anyLong());
    }
}