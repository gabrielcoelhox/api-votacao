package com.example.api_votacao.service;

import com.example.api_votacao.entity.Cargo;
import com.example.api_votacao.repository.CargoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoService.class);

    @Autowired
    private CargoRepository cargoRepository;

    public Cargo adicionarCargo(Cargo cargo) {
        LOGGER.info("Adicionando cargo: {}", cargo.getDescricao());
        return cargoRepository.save(cargo);
    }

    public Cargo atualizarCargo(Long id, Cargo cargo) {
        LOGGER.info("Atualizando cargo ID = {}", id);
        Cargo cargoExistente = buscarCargoPorId(id);
        cargoExistente.setDescricao(cargo.getDescricao());
        return cargoRepository.save(cargoExistente);
    }

    public List<Cargo> listarCargos() {
        LOGGER.info("Listando todos os cargos");
        return cargoRepository.findAll();
    }

    public void excluirCargo(Long cargoId) {
        LOGGER.info("Excluindo cargo ID = {}", cargoId);
        cargoRepository.deleteById(cargoId);
    }

    private Cargo buscarCargoPorId(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Cargo não encontrado: ID = {}", id);
                    return new IllegalArgumentException("Cargo não encontrado.");
                });
    }
}