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
    @DisplayName("Testes do CargoService")
    class CargoServiceTests {

        @Test
        @DisplayName("Então adiciona um cargo com sucesso")
        public void adicionarCargoTest() {
            Cargo cargo = new Cargo();
            cargo.setDescricao("Cargo 1");

            when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

            Cargo result = cargoService.adicionarCargo(cargo);

            assertEquals("Cargo 1", result.getDescricao());
            verify(cargoRepository, times(1)).save(cargo);
        }

        @Test
        @DisplayName("Então lista todos os cargos com sucesso")
        public void listarCargosTest() {
            List<Cargo> cargos = Arrays.asList(new Cargo(), new Cargo());

            when(cargoRepository.findAll()).thenReturn(cargos);

            List<Cargo> result = cargoService.listarCargos();

            assertEquals(2, result.size());
            verify(cargoRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Então exclui um cargo com sucesso")
        public void excluirCargoTest() {
            cargoService.excluirCargo(1L);

            verify(cargoRepository, times(1)).deleteById(anyLong());
        }

        @Test
        @DisplayName("Então atualiza um cargo com sucesso")
        public void atualizarCargoTest() {
            Cargo cargo = new Cargo();
            cargo.setId(1L);
            cargo.setDescricao("Descricao Atualizada");

            when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
            when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

            Cargo atualizado = cargoService.atualizarCargo(1L, cargo);

            assertEquals("Descricao Atualizada", atualizado.getDescricao());
            verify(cargoRepository, times(1)).save(cargo);
        }
    }
}