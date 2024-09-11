package com.example.api_votacao.service;

import com.example.api_votacao.entity.Eleitor;
import com.example.api_votacao.repository.EleitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EleitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EleitorService.class);

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    @Lazy
    private VotacaoService votacaoService;

    public Eleitor adicionarEleitor(Eleitor eleitor) {
        LOGGER.info("Adicionando eleitor: {}", eleitor.getNome());
        verificarExistenciaEleitor(eleitor.getNome());
        return eleitorRepository.save(eleitor);
    }

    public List<Eleitor> listarEleitores() {
        LOGGER.info("Listando todos os eleitores");
        return eleitorRepository.findAll();
    }

    public void excluirEleitor(Long eleitorId) {
        LOGGER.info("Excluindo eleitor ID = {}", eleitorId);
        votacaoService.verificarEleitorComVotos(eleitorId);
        eleitorRepository.deleteById(eleitorId);
    }

    private void verificarExistenciaEleitor(String nome) {
        if (eleitorRepository.existsByNome(nome)) {
            LOGGER.error("Eleitor já existe: {}", nome);
            throw new IllegalArgumentException("O Eleitor já existe.");
        }
    }

    protected Eleitor buscarEleitorPorId(Long eleitorId) {
        return eleitorRepository.findById(eleitorId)
                .orElseThrow(() -> {
                    LOGGER.error("Eleitor não encontrado: ID = {}", eleitorId);
                    return new IllegalArgumentException("Eleitor não encontrado.");
                });
    }
}
