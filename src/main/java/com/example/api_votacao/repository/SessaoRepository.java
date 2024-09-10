package com.example.api_votacao.repository;

import com.example.api_votacao.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    boolean existsByAbertaTrue();
}