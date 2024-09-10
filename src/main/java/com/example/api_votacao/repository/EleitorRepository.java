package com.example.api_votacao.repository;

import com.example.api_votacao.entity.Eleitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EleitorRepository extends JpaRepository<Eleitor, Long> {
}