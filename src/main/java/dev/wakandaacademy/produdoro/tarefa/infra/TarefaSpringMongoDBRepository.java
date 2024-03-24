package dev.wakandaacademy.produdoro.tarefa.infra;

import dev.wakandaacademy.produdoro.tarefa.domain.StatusTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarefaSpringMongoDBRepository extends MongoRepository<Tarefa, UUID> {
    Optional<Tarefa> findByIdTarefa(UUID idTarefa);
    List<Tarefa> findAllByIdUsuarioOrderByPosicaoAsc(UUID idUsuario);
    int countByIdUsuario(UUID idUsuario);
    List<Tarefa> findAllByStatus(StatusTarefa statusTarefa);
}