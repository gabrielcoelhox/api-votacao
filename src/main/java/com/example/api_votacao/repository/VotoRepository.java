package com.example.api_votacao.repository;

import com.example.api_votacao.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByEleitorId(Long eleitorId);
    long countCandidatoById(Long candidatoId);

    List<Voto> findBySessaoId(Long sessaoId);
}