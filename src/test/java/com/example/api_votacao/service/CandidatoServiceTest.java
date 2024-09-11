package com.example.api_votacao.service;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.repository.CandidatoRepository;
import com.example.api_votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes do CandidatoService")
class CandidatoServiceTest {

    @InjectMocks
    private CandidatoService candidatoService;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private VotoRepository votoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testes do CandidatoService")
    class CandidatoServiceTests {

        @Test
        @DisplayName("Então adiciona um candidato com sucesso")
        public void adicionarCandidatoTest() {
            Candidato candidato = new Candidato();
            candidato.setNome("Candidato 1");

            when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

            Candidato result = candidatoService.adicionarCandidato(candidato);

            assertEquals("Candidato 1", result.getNome());
            verify(candidatoRepository, times(1)).save(candidato);
        }

        @Test
        @DisplayName("Então lista todos os candidatos com sucesso")
        public void listarCandidatosTest() {
            List<Candidato> candidatos = Arrays.asList(new Candidato(), new Candidato());

            when(candidatoRepository.findAll()).thenReturn(candidatos);

            List<Candidato> result = candidatoService.listarCandidatos();

            assertEquals(2, result.size());
            verify(candidatoRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Então exclui um candidato com sucesso")
        public void excluirCandidatoTest() {
            when(votoRepository.countCandidatoById(anyLong())).thenReturn(0L);

            candidatoService.excluirCandidato(1L);

            verify(candidatoRepository, times(1)).deleteById(anyLong());
        }
    }
}