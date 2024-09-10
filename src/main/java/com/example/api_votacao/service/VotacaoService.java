package com.example.api_votacao.service;

import com.example.api_votacao.entity.*;
import com.example.api_votacao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotacaoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    private VotoRepository votoRepository;

    public Candidato adicionarCandidato(Candidato candidato) {
        if (candidatoRepository.existsByNome(candidato.getNome())) {
            throw new IllegalArgumentException("Candidato já existe.");
        }
        return candidatoRepository.save(candidato);
    }

    public Eleitor adicionarEleitor(Eleitor eleitor) {
        if (eleitorRepository.existsByNome(eleitor.getNome())) {
            throw new IllegalArgumentException("Eleitor já existe.");
        }
        return eleitorRepository.save(eleitor);
    }

    public List<Candidato> listarCandidatos() {
        return candidatoRepository.findAll();
    }

    public List<Eleitor> listarEleitores() {
        return eleitorRepository.findAll();
    }

    public List<Voto> listarVotos() {
        return votoRepository.findAll();
    }
}