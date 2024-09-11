package com.example.api_votacao.service;

import com.example.api_votacao.entity.Eleitor;
import com.example.api_votacao.repository.EleitorRepository;
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

@DisplayName("Testes do EleitorService")
public class EleitorServiceTest {

    @InjectMocks
    private EleitorService eleitorService;

    @Mock
    private EleitorRepository eleitorRepository;

    @Mock
    private VotacaoService votacaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testes de adicionar eleitor")
    class AdicionarEleitorTests {

        @Test
        @DisplayName("Deve adicionar um eleitor com sucesso")
        public void adicionarEleitorComSucesso() {
            Eleitor eleitor = new Eleitor();
            eleitor.setNome("Eleitor 1");

            when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

            Eleitor result = eleitorService.adicionarEleitor(eleitor);

            assertEquals("Eleitor 1", result.getNome());
            verify(eleitorRepository, times(1)).save(eleitor);
        }
    }

    @Nested
    @DisplayName("Testes de listar eleitores")
    class ListarEleitoresTests {

        @Test
        @DisplayName("Deve listar todos os eleitores com sucesso")
        public void listarEleitoresComSucesso() {
            List<Eleitor> eleitores = Arrays.asList(new Eleitor(), new Eleitor());

            when(eleitorRepository.findAll()).thenReturn(eleitores);

            List<Eleitor> result = eleitorService.listarEleitores();

            assertEquals(2, result.size());
            verify(eleitorRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de excluir eleitor")
    class ExcluirEleitorTests {

        @Test
        @DisplayName("Deve excluir um eleitor com sucesso")
        public void excluirEleitorComSucesso() {
            doNothing().when(votacaoService).verificarEleitorComVotos(anyLong());

            eleitorService.excluirEleitor(1L);

            verify(eleitorRepository, times(1)).deleteById(anyLong());
        }

        @Test
        @DisplayName("Deve lançar exceção ao excluir eleitor com votos")
        public void excluirEleitorComVotos() {
            doThrow(new IllegalArgumentException("Eleitor não pode ser excluído, pois possui votos."))
                    .when(votacaoService).verificarEleitorComVotos(anyLong());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                eleitorService.excluirEleitor(1L);
            });

            assertEquals("Eleitor não pode ser excluído, pois possui votos.", exception.getMessage());
            verify(eleitorRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Testes de buscar eleitor por ID")
    class BuscarEleitorPorIdTests {

        @Test
        @DisplayName("Deve buscar eleitor por ID com sucesso")
        public void buscarEleitorPorIdComSucesso() {
            Eleitor eleitor = new Eleitor();
            eleitor.setId(1L);

            when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

            Eleitor result = eleitorService.buscarEleitorPorId(1L);

            assertEquals(1L, result.getId());
            verify(eleitorRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar eleitor inexistente por ID")
        public void buscarEleitorInexistentePorId() {
            when(eleitorRepository.findById(anyLong())).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                eleitorService.buscarEleitorPorId(1L);
            });

            assertEquals("Eleitor não encontrado.", exception.getMessage());
            verify(eleitorRepository, times(1)).findById(anyLong());
        }
    }
}