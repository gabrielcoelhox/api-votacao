package com.example.api_votacao.service;

import com.example.api_votacao.entity.Sessao;
import com.example.api_votacao.repository.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Testes do SessaoService")
public class SessaoServiceTest {

    @InjectMocks
    private SessaoService sessaoService;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private VotacaoService votacaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testes de abrir sessão")
    class AbrirSessaoTests {

        @Test
        @DisplayName("Deve abrir uma sessão com sucesso")
        public void abrirSessaoComSucesso() {
            Sessao sessao = new Sessao();
            sessao.setInicio(LocalDateTime.now());
            sessao.setAberta(true);

            when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

            Sessao result = sessaoService.abrirSessao();

            assertTrue(result.isAberta());
            assertNotNull(result.getInicio());
            verify(sessaoRepository, times(1)).save(any(Sessao.class));
        }
    }

    @Nested
    @DisplayName("Testes de fechar sessão")
    class FecharSessaoTests {

        @Test
        @DisplayName("Deve fechar uma sessão com sucesso")
        public void fecharSessaoComSucesso() {
            Sessao sessao = new Sessao();
            sessao.setId(1L);
            sessao.setInicio(LocalDateTime.now());
            sessao.setAberta(true);

            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.of(sessao));
            when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

            Sessao result = sessaoService.fecharSessao(1L);

            assertFalse(result.isAberta());
            assertNotNull(result.getFim());
            verify(sessaoRepository, times(1)).findById(anyLong());
            verify(sessaoRepository, times(1)).save(sessao);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar fechar sessão inexistente")
        public void fecharSessaoInexistente() {
            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                sessaoService.fecharSessao(1L);
            });

            assertEquals("Sessão não encontrada.", exception.getMessage());
            verify(sessaoRepository, times(1)).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("Testes de buscar sessão")
    class BuscarSessaoTests {

        @Test
        @DisplayName("Deve buscar sessão por ID com sucesso")
        public void buscarSessaoPorIdComSucesso() {
            Sessao sessao = new Sessao();
            sessao.setId(1L);

            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.of(sessao));

            Sessao result = sessaoService.buscarSessao(1L);

            assertEquals(1L, result.getId());
            verify(sessaoRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar sessão inexistente por ID")
        public void buscarSessaoInexistentePorId() {
            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                sessaoService.buscarSessao(1L);
            });

            assertEquals("Sessão não encontrada.", exception.getMessage());
            verify(sessaoRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("Deve buscar sessão aberta com sucesso")
        public void buscarSessaoAbertaComSucesso() {
            Sessao sessao = new Sessao();
            sessao.setAberta(true);

            when(sessaoRepository.findByAbertaTrue()).thenReturn(Optional.of(sessao));

            Sessao result = sessaoService.buscarSessaoAberta();

            assertTrue(result.isAberta());
            verify(sessaoRepository, times(1)).findByAbertaTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar sessão aberta inexistente")
        public void buscarSessaoAbertaInexistente() {
            when(sessaoRepository.findByAbertaTrue()).thenReturn(Optional.empty());

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                sessaoService.buscarSessaoAberta();
            });

            assertEquals("Nenhuma sessão aberta encontrada", exception.getMessage());
            verify(sessaoRepository, times(1)).findByAbertaTrue();
        }
    }
}