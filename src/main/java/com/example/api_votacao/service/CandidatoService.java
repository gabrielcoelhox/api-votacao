package com.example.api_votacao.service;

import com.example.api_votacao.entity.Candidato;
import com.example.api_votacao.repository.CandidatoRepository;
import com.example.api_votacao.repository.VotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandidatoService.class);

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VotoRepository votoRepository;

    public Candidato adicionarCandidato(Candidato candidato) {
        LOGGER.info("Adicionando candidato: {}", candidato.getNome());
        verificarExistenciaCandidato(candidato.getNome());
        return candidatoRepository.save(candidato);
    }

    public List<Candidato> listarCandidatos() {
        LOGGER.info("Listando todos os candidatos");
        return candidatoRepository.findAll();
    }

    public void excluirCandidato(Long candidatoId) {
        LOGGER.info("Excluindo candidato ID = {}", candidatoId);
        verificarCandidatoComVotos(candidatoId);
        candidatoRepository.deleteById(candidatoId);
    }

    private void verificarExistenciaCandidato(String nome) {
        if (candidatoRepository.existsByNome(nome)) {
            LOGGER.error("Candidato já existe: {}", nome);
            throw new IllegalArgumentException("O Candidato já existe.");
        }
    }

    protected Candidato buscarCandidatoPorId(Long candidatoId) {
        return candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> {
                    LOGGER.error("Candidato não encontrado: ID = {}", candidatoId);
                    return new IllegalArgumentException("Candidato não encontrado.");
                });
    }

    private void verificarCandidatoComVotos(Long candidatoId) {
        if (votoRepository.countCandidatoById(candidatoId) > 0) {
            LOGGER.error("Candidato não pode ser excluído, pois possui votos: ID = {}", candidatoId);
            throw new IllegalArgumentException("Candidato não pode ser excluído, pois possui votos.");
        }
    }
}
