package com.example.api_votacao.repository;

import com.example.api_votacao.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    boolean existsByAbertaTrue();
    Optional<Sessao> findByAbertaTrue();
}