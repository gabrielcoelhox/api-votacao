package com.example.api_votacao.service;

import com.example.api_votacao.entity.*;
import com.example.api_votacao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VotacaoService {

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
        verificarExistenciaCandidato(candidato.getNome());
        return candidatoRepository.save(candidato);
    }

    public Eleitor adicionarEleitor(Eleitor eleitor) {
        verificarExistenciaEleitor(eleitor.getNome());
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

    public Voto adicionarVoto(Long eleitorId, Long candidatoId) {
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
        verificarSessaoAberta();
        Sessao sessao = new Sessao();
        sessao.setInicio(LocalDateTime.now());
        sessao.setAberta(true);
        return sessaoRepository.save(sessao);
    }

    public Sessao fecharSessao(Long sessaoId) {
        Sessao sessao = buscarSessaoPorId(sessaoId);
        verificarSessaoFechada(sessao);
        verificarQuantidadeVotos();

        sessao.setFim(LocalDateTime.now());
        sessao.setAberta(false);
        return sessaoRepository.save(sessao);
    }

    public Sessao buscarSessao(Long idSessao) {
        return buscarSessaoPorId(idSessao);
    }

    public List<Voto> buscarVotosPorSessao(Long idSessao) {
        return votoRepository.findBySessaoId(idSessao);
    }

    public void excluirCandidato(Long candidatoId) {
        verificarCandidatoComVotos(candidatoId);
        candidatoRepository.deleteById(candidatoId);
    }

    public void excluirEleitor(Long eleitorId) {
        verificarEleitorComVotos(eleitorId);
        eleitorRepository.deleteById(eleitorId);
    }

    public Cargo adicionarCargo(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    public Cargo atualizarCargo(Long id, Cargo cargo) {
        Cargo cargoExistente = buscarCargoPorId(id);
        cargoExistente.setDescricao(cargo.getDescricao());
        return cargoRepository.save(cargoExistente);
    }

    public List<Cargo> listarCargos() {
        return cargoRepository.findAll();
    }

    public void excluirCargo(Long cargoId) {
        cargoRepository.deleteById(cargoId);
    }

    public String gerarBoletimUrna(Long idSessao) {
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
            throw new IllegalArgumentException("O Candidato já existe.");
        }
    }

    private void verificarExistenciaEleitor(String nome) {
        if (eleitorRepository.existsByNome(nome)) {
            throw new IllegalArgumentException("O Eleitor já existe.");
        }
    }

    private void verificarSessaoAberta() {
        if (sessaoRepository.existsByAbertaTrue()) {
            throw new IllegalStateException("Já existe uma sessão aberta.");
        }
    }

    private void verificarEleitorJaVotou(Long eleitorId) {
        if (votoRepository.existsByEleitorId(eleitorId)) {
            throw new IllegalArgumentException("O Eleitor já votou.");
        }
    }

    private Candidato buscarCandidatoPorId(Long candidatoId) {
        return candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new IllegalArgumentException("Candidato não encontrado."));
    }

    private Eleitor buscarEleitorPorId(Long eleitorId) {
        return eleitorRepository.findById(eleitorId)
                .orElseThrow(() -> new IllegalArgumentException("Eleitor não encontrado."));
    }

    private Cargo buscarCargoPorId(Long id) {
        Optional<Cargo> cargo = cargoRepository.findById(id);
        if (cargo.isPresent()) {
            return cargo.get();
        } else {
            throw new IllegalArgumentException("Cargo não encontrado.");
        }
    }

    private Sessao buscarSessaoPorId(Long sessaoId) {
        return sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada."));
    }

    private void verificarSessaoFechada(Sessao sessao) {
        if (!sessao.isAberta()) {
            throw new IllegalArgumentException("Sessão já está fechada.");
        }
    }

    private void verificarQuantidadeVotos() {
        long votos = votoRepository.count();
        if (votos == 1) {
            throw new IllegalArgumentException("Sessão não pode ser encerrada com 1 voto.");
        }
    }

    private void verificarCandidatoComVotos(Long candidatoId) {
        if (votoRepository.countCandidatoById(candidatoId) > 0) {
            throw new IllegalArgumentException("Candidato não pode ser excluído, pois possui votos.");
        }
    }

    private void verificarEleitorComVotos(Long eleitorId) {
        if (votoRepository.existsByEleitorId(eleitorId)) {
            throw new IllegalArgumentException("Eleitor não pode ser excluído, pois possui votos.");
        }
    }

    private void verificarSessaoEncerrada(Sessao sessao) {
        if (sessao.isAberta() || sessao.getFim() == null) {
            throw new IllegalArgumentException("Sessão não encerrada ou não encontrada.");
        }
    }
}