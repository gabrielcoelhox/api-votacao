package com.example.api_votacao.repository;

import com.example.api_votacao.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByEleitorId(Long eleitorId);
    long countByCandidatoId(Long candidatoId);
}