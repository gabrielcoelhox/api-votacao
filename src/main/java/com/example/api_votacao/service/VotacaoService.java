package com.example.api_votacao.service;

import com.example.api_votacao.entity.*;
import com.example.api_votacao.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VotacaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoService.class);

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EleitorRepository eleitorRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    public Candidato adicionarCandidato(Candidato candidato) {
        LOGGER.info("Adicionando candidato: {}", candidato.getNome());
        verificarExistenciaCandidato(candidato.getNome());
        return candidatoRepository.save(candidato);
    }

    public Eleitor adicionarEleitor(Eleitor eleitor) {
        LOGGER.info("Adicionando eleitor: {}", eleitor.getNome());
        verificarExistenciaEleitor(eleitor.getNome());
        return eleitorRepository.save(eleitor);
    }

    public List<Candidato> listarCandidatos() {
        LOGGER.info("Listando todos os candidatos");
        return candidatoRepository.findAll();
    }

    public List<Eleitor> listarEleitores() {
        LOGGER.info("Listando todos os eleitores");
        return eleitorRepository.findAll();
    }

    public List<Voto> listarVotos() {
        LOGGER.info("Listando todos os votos");
        return votoRepository.findAll();
    }

    public Voto adicionarVoto(Long eleitorId, Long candidatoId) {
        LOGGER.info("Adicionando voto: Eleitor ID = {}, Candidato ID = {}", eleitorId, candidatoId);
        verificarSessaoAberta();
        verificarEleitorJaVotou(eleitorId);

        Candidato candidato = buscarCandidatoPorId(candidatoId);
        Eleitor eleitor = buscarEleitorPorId(eleitorId);

        Voto voto = new Voto();
        voto.setCandidato(candidato);
        voto.setEleitor(eleitor);
        return votoRepository.save(voto);
    }

    public Sessao abrirSessao() {
        LOGGER.info("Abrindo nova sessão");
        verificarSessaoAberta();
        Sessao sessao = new Sessao();
        sessao.setInicio(LocalDateTime.now());
        sessao.setAberta(true);
        return sessaoRepository.save(sessao);
    }

    public Sessao fecharSessao(Long sessaoId) {
        LOGGER.info("Fechando sessão ID = {}", sessaoId);
        Sessao sessao = buscarSessaoPorId(sessaoId);
        verificarSessaoFechada(sessao);
        verificarQuantidadeVotos();

        sessao.setFim(LocalDateTime.now());
        sessao.setAberta(false);
        return sessaoRepository.save(sessao);
    }

    public Sessao buscarSessao(Long idSessao) {
        LOGGER.info("Buscando sessão ID = {}", idSessao);
        return buscarSessaoPorId(idSessao);
    }

    public List<Voto> buscarVotosPorSessao(Long idSessao) {
        LOGGER.info("Buscando votos para a sessão ID = {}", idSessao);
        return votoRepository.findBySessaoId(idSessao);
    }

    public void excluirCandidato(Long candidatoId) {
        LOGGER.info("Excluindo candidato ID = {}", candidatoId);
        verificarCandidatoComVotos(candidatoId);
        candidatoRepository.deleteById(candidatoId);
    }

    public void excluirEleitor(Long eleitorId) {
        LOGGER.info("Excluindo eleitor ID = {}", eleitorId);
        verificarEleitorComVotos(eleitorId);
        eleitorRepository.deleteById(eleitorId);
    }

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

    public String gerarBoletimUrna(Long idSessao) {
        LOGGER.info("Gerando boletim de urna para a sessão ID = {}", idSessao);
        Sessao sessao = buscarSessaoPorId(idSessao);
        verificarSessaoEncerrada(sessao);

        List<Voto> votos = buscarVotosPorSessao(idSessao);
        Map<Candidato, Long> votosPorCandidato = votos.stream()
                .collect(Collectors.groupingBy(Voto::getCandidato, Collectors.counting()));

        StringBuilder boletim = new StringBuilder();
        boletim.append(String.format("%-40s\n", "Boletim de Urna"));
        boletim.append(String.format("%-40s\n", "--------------------"));
        votosPorCandidato.forEach((candidato, count) -> {
            boletim.append(String.format("%-30s %10d\n", candidato.getNome(), count));
        });

        return boletim.toString();
    }

    private void verificarExistenciaCandidato(String nome) {
        if (candidatoRepository.existsByNome(nome)) {
            LOGGER.error("Candidato já existe: {}", nome);
            throw new IllegalArgumentException("O Candidato já existe.");
        }
    }

    private void verificarExistenciaEleitor(String nome) {
        if (eleitorRepository.existsByNome(nome)) {
            LOGGER.error("Eleitor já existe: {}", nome);
            throw new IllegalArgumentException("O Eleitor já existe.");
        }
    }

    private void verificarSessaoAberta() {
        if (sessaoRepository.existsByAbertaTrue()) {
            LOGGER.error("Já existe uma sessão aberta");
            throw new IllegalStateException("Já existe uma sessão aberta.");
        }
    }

    private void verificarEleitorJaVotou(Long eleitorId) {
        if (votoRepository.existsByEleitorId(eleitorId)) {
            LOGGER.error("Eleitor já votou: ID = {}", eleitorId);
            throw new IllegalArgumentException("O Eleitor já votou.");
        }
    }

    private Candidato buscarCandidatoPorId(Long candidatoId) {
        return candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> {
                    LOGGER.error("Candidato não encontrado: ID = {}", candidatoId);
                    return new IllegalArgumentException("Candidato não encontrado.");
                });
    }

    private Eleitor buscarEleitorPorId(Long eleitorId) {
        return eleitorRepository.findById(eleitorId)
                .orElseThrow(() -> {
                    LOGGER.error("Eleitor não encontrado: ID = {}", eleitorId);
                    return new IllegalArgumentException("Eleitor não encontrado.");
                });
    }

    private Cargo buscarCargoPorId(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Cargo não encontrado: ID = {}", id);
                    return new IllegalArgumentException("Cargo não encontrado.");
                });
    }

    private Sessao buscarSessaoPorId(Long sessaoId) {
        return sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> {
                    LOGGER.error("Sessão não encontrada: ID = {}", sessaoId);
                    return new IllegalArgumentException("Sessão não encontrada.");
                });
    }

    private void verificarSessaoFechada(Sessao sessao) {
        if (!sessao.isAberta()) {
            LOGGER.error("Sessão já está fechada: ID = {}", sessao.getId());
            throw new IllegalArgumentException("Sessão já está fechada.");
        }
    }

    private void verificarQuantidadeVotos() {
        long votos = votoRepository.count();
        if (votos == 1) {
            LOGGER.error("Sessão não pode ser encerrada com 1 voto");
            throw new IllegalArgumentException("Sessão não pode ser encerrada com 1 voto.");
        }
    }

    private void verificarCandidatoComVotos(Long candidatoId) {
        if (votoRepository.countCandidatoById(candidatoId) > 0) {
            LOGGER.error("Candidato não pode ser excluído, pois possui votos: ID = {}", candidatoId);
            throw new IllegalArgumentException("Candidato não pode ser excluído, pois possui votos.");
        }
    }

    private void verificarEleitorComVotos(Long eleitorId) {
        if (votoRepository.existsByEleitorId(eleitorId)) {
            LOGGER.error("Eleitor não pode ser excluído, pois possui votos: ID = {}", eleitorId);
            throw new IllegalArgumentException("Eleitor não pode ser excluído, pois possui votos.");
        }
    }

    private void verificarSessaoEncerrada(Sessao sessao) {
        if (sessao.isAberta() || sessao.getFim() == null) {
            LOGGER.error("Sessão não encerrada ou não encontrada: ID = {}", sessao.getId());
            throw new IllegalArgumentException("Sessão não encerrada ou não encontrada.");
        }
    }
}