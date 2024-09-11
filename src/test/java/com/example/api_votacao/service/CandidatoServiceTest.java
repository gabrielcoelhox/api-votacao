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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    @DisplayName("Testes de adicionar candidato")
    class AdicionarCandidatoTests {

        @Test
        @DisplayName("Deve adicionar um candidato com sucesso")
        public void adicionarCandidatoComSucesso() {
            Candidato candidato = new Candidato();
            candidato.setNome("Candidato 1");

            when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

            Candidato result = candidatoService.adicionarCandidato(candidato);

            assertEquals("Candidato 1", result.getNome());
            verify(candidatoRepository, times(1)).save(candidato);
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar candidato existente")
        public void adicionarCandidatoExistente() {
            Candidato candidato = new Candidato();
            candidato.setNome("Candidato 1");

            when(candidatoRepository.existsByNome(anyString())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                candidatoService.adicionarCandidato(candidato);
            });

            assertEquals("O Candidato já existe.", exception.getMessage());
            verify(candidatoRepository, never()).save(any(Candidato.class));
        }
    }

    @Nested
    @DisplayName("Testes de listar candidatos")
    class ListarCandidatosTests {

        @Test
        @DisplayName("Deve listar todos os candidatos com sucesso")
        public void listarCandidatosComSucesso() {
            List<Candidato> candidatos = Arrays.asList(new Candidato(), new Candidato());

            when(candidatoRepository.findAll()).thenReturn(candidatos);

            List<Candidato> result = candidatoService.listarCandidatos();

            assertEquals(2, result.size());
            verify(candidatoRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de excluir candidato")
    class ExcluirCandidatoTests {

        @Test
        @DisplayName("Deve excluir um candidato com sucesso")
        public void excluirCandidatoComSucesso() {
            when(votoRepository.countCandidatoById(anyLong())).thenReturn(0L);

            candidatoService.excluirCandidato(1L);

            verify(candidatoRepository, times(1)).deleteById(anyLong());
        }

        @Test
        @DisplayName("Deve lançar exceção ao excluir candidato com votos")
        public void excluirCandidatoComVotos() {
            when(votoRepository.countCandidatoById(anyLong())).thenReturn(1L);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                candidatoService.excluirCandidato(1L);
            });

            assertEquals("Candidato não pode ser excluído, pois possui votos.", exception.getMessage());
            verify(candidatoRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Testes de buscar candidato por ID")
    class BuscarCandidatoPorIdTests {

        @Test
        @DisplayName("Deve buscar candidato por ID com sucesso")
        public void buscarCandidatoPorIdComSucesso() {
            Candidato candidato = new Candidato();
            candidato.setId(1L);

            when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidato));

            Candidato result = candidatoService.buscarCandidatoPorId(1L);

            assertEquals(1L, result.getId());
            verify(candidatoRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar candidato inexistente por ID")
        public void buscarCandidatoInexistentePorId() {
            when(candidatoRepository.findById(anyLong())).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                candidatoService.buscarCandidatoPorId(1L);
            });

            assertEquals("Candidato não encontrado.", exception.getMessage());
            verify(candidatoRepository, times(1)).findById(anyLong());
        }
    }
}