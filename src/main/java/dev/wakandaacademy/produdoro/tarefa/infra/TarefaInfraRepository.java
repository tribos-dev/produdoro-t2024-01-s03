package dev.wakandaacademy.produdoro.tarefa.infra;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class TarefaInfraRepository implements TarefaRepository {

    private final TarefaSpringMongoDBRepository tarefaSpringMongoDBRepository;

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
    public List<Tarefa> buscaTarefasConcluidasDoUsuario() {
        log.info("[inicia] TarefaInfraRepository - buscaTarefasConcluidasDoUsuario");
        List<Tarefa> tarefasConcluidas = tarefaSpringMongoDBRepository.findAllByStatus(StatusTarefa.CONCLUIDA);
        if (tarefasConcluidas.isEmpty()) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Não existem tarefas concluidas");
        }
        log.info("[finaliza] TarefaInfraRepository - buscaTarefasConcluidasDoUsuario");
        return tarefasConcluidas;
    }
    @Override
    public void deletaTodasAsTarefasConcluidas(List<Tarefa> tarefasConcluidas) {
        log.info("[inicia] TarefaInfraRepository - deletaTodasAsTarefasConcluidas");
        tarefaSpringMongoDBRepository.deleteAll(tarefasConcluidas);
        log.info("[finaliza] TarefaInfraRepository - deletaTodasAsTarefasConcluidas");
    }
    public List<Tarefa> buscaTodasSuasTarefa(UUID IdUsuario) {
        log.info("[inicial] - TarefaInfraRepository - buscaTodasSuasTarefa");
        List<Tarefa> tarefaList = tarefaSpringMongoDBRepository.findAllByIdUsuario(IdUsuario);
        log.info("[finaliza] - TarefaInfraRepository - buscaTodasSuasTarefa");
        return tarefaList;
    }
}