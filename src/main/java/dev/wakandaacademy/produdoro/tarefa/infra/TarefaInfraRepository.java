package dev.wakandaacademy.produdoro.tarefa.infra;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.NovaPosicaoDaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class TarefaInfraRepository implements TarefaRepository {

    private final TarefaSpringMongoDBRepository tarefaSpringMongoDBRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Tarefa salva(Tarefa tarefa) {
        log.info("[inicia] TarefaInfraRepository - salva");
        try {
            tarefaSpringMongoDBRepository.save(tarefa);
        } catch (DataIntegrityViolationException e) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Tarefa já cadastrada", e);
        }
        log.info("[finaliza] TarefaInfraRepository - salva");
        return tarefa;
    }
    @Override
    public Optional<Tarefa> buscaTarefaPorId(UUID idTarefa) {
        log.info("[inicia] TarefaInfraRepository - buscaTarefaPorId");
        Optional<Tarefa> tarefaPorId = tarefaSpringMongoDBRepository.findByIdTarefa(idTarefa);
        log.info("[finaliza] TarefaInfraRepository - buscaTarefaPorId");
        return tarefaPorId;
    }

    @Override
    public List<Tarefa> buscaTodasAsTarefasDoUsuario(UUID idUsuario) {
        log.info("[inicia] TarefaInfraRepository - buscaTodasAsTarefasDoUsuario");
        List<Tarefa> tarefas = tarefaSpringMongoDBRepository.findAllByIdUsuario(idUsuario);
        log.info("[finaliza] TarefaInfraRepository - buscaTodasAsTarefasDoUsuario");
        return tarefas;
    }

    @Override
    public void salvaVariasTarefas(List<Tarefa> tarefasComNovasPosicoes) {
        log.info("[inicia] TarefaInfraRepository - salvaVariasTarefas");
        tarefaSpringMongoDBRepository.saveAll(tarefasComNovasPosicoes);
        log.info("[finaliza] TarefaInfraRepository - salvaVariasTarefas");
    }

    @Override
    public int contarTarefasDoUsuario(UUID idUsuario) {
        log.info("[inicia] TarefaInfraRepository - contarTarefasDoUsuario");
        int contarTarefas = tarefaSpringMongoDBRepository.countByIdUsuario(idUsuario);
        log.info("[finaliza] TarefaInfraRepository - contarTarefasDoUsuario");
        return contarTarefas;
    }

    public void defineNovaPosicaoDaTarefa(Tarefa tarefa, List<Tarefa> tarefas, NovaPosicaoDaTarefaRequest novaPosicao) {
        validaNovaPosicao(tarefas, tarefa, novaPosicao);
        int posicaoAntiga = tarefa.getPosicao();
        int novaPosicaoInt = novaPosicao.getNovaPosicao();

        List<Tarefa> tarefasComNovasPosicoes = IntStream.range(Math.min(novaPosicaoInt, posicaoAntiga), Math.max(novaPosicaoInt, posicaoAntiga))
                .mapToObj(i -> atualizaPosicaoTarefa(tarefas.get(i), novaPosicaoInt < posicaoAntiga, tarefas))
                .collect(Collectors.toList());
        atualizaPosicaoTarefa(tarefa, novaPosicaoInt > posicaoAntiga, tarefas);
        tarefa.setPosicao(novaPosicaoInt);
        salva(tarefa);
        salvaVariasTarefas(tarefasComNovasPosicoes);
    }

    private Tarefa atualizaPosicaoTarefa(Tarefa tarefa, boolean incrementa, List<Tarefa> tarefas) {
        if (incrementa) tarefa.incrementaPosicao(tarefas.size());
        else tarefa.decrementaPosicao();

        Query queryAtualizacao = new Query(Criteria.where("idTarefa").is(tarefa.getIdTarefa()));
        Update updateAtualizacao = new Update().set("posicao", tarefa.getPosicao());
        mongoTemplate.updateFirst(queryAtualizacao, updateAtualizacao, Tarefa.class);
        return tarefa;
    }

    private void validaNovaPosicao(List<Tarefa> tarefas,Tarefa tarefa, NovaPosicaoDaTarefaRequest novaPosicao) {
        int posicaoAntiga = tarefa.getPosicao();
        int tamanhoDaLista = tarefas.size();

        if (novaPosicao.getNovaPosicao() >= tamanhoDaLista || Objects.equals(novaPosicao.getNovaPosicao(), posicaoAntiga)) {
            String mensagem = novaPosicao.getNovaPosicao() >= tamanhoDaLista ?
                    "A posição da tarefa não pode ser igual, nem maior que a quantidade total de tarefas" :
                    "A posição enviada é igual à já presente, insira uma nova.";
            throw APIException.build(HttpStatus.BAD_REQUEST, mensagem);
        }
    }

    public List<Tarefa> buscaTodasSuasTarefa(UUID IdUsuario) {
        log.info("[inicial] - TarefaInfraRepository - buscaTodasSuasTarefa");
        List<Tarefa> tarefaList = tarefaSpringMongoDBRepository.findAllByIdUsuario(IdUsuario);

        log.info("[finaliza] - TarefaInfraRepository - buscaTodasSuasTarefa");
        return tarefaList;
    }
}
