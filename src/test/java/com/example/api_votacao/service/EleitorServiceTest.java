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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("Testes do CargoService")
    class EleitorServiceTests {

        @Test
        @DisplayName("Então adiciona um eleitor com sucesso")
        public void adicionarEleitorTest() {
            Eleitor eleitor = new Eleitor();
            eleitor.setNome("Eleitor 1");

            when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

            Eleitor result = eleitorService.adicionarEleitor(eleitor);

            assertEquals("Eleitor 1", result.getNome());
            verify(eleitorRepository, times(1)).save(eleitor);
        }

        @Test
        @DisplayName("Então lista todos os eleitores com sucesso")
        public void listarEleitoresTest() {
            List<Eleitor> eleitores = Arrays.asList(new Eleitor(), new Eleitor());

            when(eleitorRepository.findAll()).thenReturn(eleitores);

            List<Eleitor> result = eleitorService.listarEleitores();

            assertEquals(2, result.size());
            verify(eleitorRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Então exclui um eleitor com sucesso")
        public void excluirEleitorTest() {
            doNothing().when(votacaoService).verificarEleitorComVotos(anyLong());

            eleitorService.excluirEleitor(1L);

            verify(eleitorRepository, times(1)).deleteById(anyLong());
        }
    }
}