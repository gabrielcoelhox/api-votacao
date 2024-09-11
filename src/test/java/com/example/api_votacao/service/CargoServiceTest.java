package com.example.api_votacao.service;

import com.example.api_votacao.entity.Cargo;
import com.example.api_votacao.repository.CargoRepository;
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

@DisplayName("Testes do CargoService")
public class CargoServiceTest {

    @InjectMocks
    private CargoService cargoService;

    @Mock
    private CargoRepository cargoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testes de adicionar cargo")
    class AdicionarCargoTests {

        @Test
        @DisplayName("Deve adicionar um cargo com sucesso")
        public void adicionarCargoComSucesso() {
            Cargo cargo = new Cargo();
            cargo.setNome("Cargo 1");

            when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

            Cargo result = cargoService.adicionarCargo(cargo);

            assertEquals("Cargo 1", result.getNome());
            verify(cargoRepository, times(1)).save(cargo);
        }
    }

    @Nested
    @DisplayName("Testes de listar cargos")
    class ListarCargosTests {

        @Test
        @DisplayName("Deve listar todos os cargos com sucesso")
        public void listarCargosComSucesso() {
            List<Cargo> cargos = Arrays.asList(new Cargo(), new Cargo());

            when(cargoRepository.findAll()).thenReturn(cargos);

            List<Cargo> result = cargoService.listarCargos();

            assertEquals(2, result.size());
            verify(cargoRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de excluir cargo")
    class ExcluirCargoTests {

        @Test
        @DisplayName("Deve excluir um cargo com sucesso")
        public void excluirCargoComSucesso() {
            cargoService.excluirCargo(1L);

            verify(cargoRepository, times(1)).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Testes de atualizar cargo")
    class AtualizarCargoTests {

        @Test
        @DisplayName("Deve atualizar um cargo com sucesso")
        public void atualizarCargoComSucesso() {
            Cargo cargo = new Cargo();
            cargo.setId(1L);
            cargo.setNome("Descricao Atualizada");

            when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
            when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

            Cargo atualizado = cargoService.atualizarCargo(1L, cargo);

            assertEquals("Descricao Atualizada", atualizado.getNome());
            verify(cargoRepository, times(1)).save(cargo);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar atualizar cargo inexistente")
        public void atualizarCargoInexistente() {
            Cargo cargo = new Cargo();
            cargo.setId(1L);
            cargo.setNome("Descricao Atualizada");

            when(cargoRepository.findById(1L)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                cargoService.atualizarCargo(1L, cargo);
            });

            assertEquals("Cargo não encontrado.", exception.getMessage());
            verify(cargoRepository, never()).save(any(Cargo.class));
        }
    }
}