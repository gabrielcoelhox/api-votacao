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
    @DisplayName("Testes do SessaoService")
    class SessaoServiceTests {
        @Test
        @DisplayName("Então abre uma sessão com sucesso")
        public void abrirSessaoTest() {
            Sessao sessao = new Sessao();
            sessao.setAberta(true);

            when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

            Sessao result = sessaoService.abrirSessao();

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
            doNothing().when(votacaoService).verificarQuantidadeVotos();

            Sessao result = sessaoService.fecharSessao(1L);

            assertFalse(result.isAberta());
            verify(sessaoRepository, times(1)).findById(anyLong());
            verify(sessaoRepository, times(1)).save(any(Sessao.class));
        }

        @Test
        @DisplayName("Então busca uma sessão com sucesso")
        public void buscarSessaoTest() {
            Sessao sessao = new Sessao();

            when(sessaoRepository.findById(anyLong())).thenReturn(Optional.of(sessao));

            Sessao result = sessaoService.buscarSessao(1L);

            assertNotNull(result);
            verify(sessaoRepository, times(1)).findById(anyLong());
        }
    }
}