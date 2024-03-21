package dev.wakandaacademy.produdoro.tarefa.infra;

import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarefaSpringMongoDBRepository extends MongoRepository<Tarefa, UUID> {
    Optional<Tarefa> findByIdTarefa(UUID idTarefa);

<<<<<<< HEAD
    void deleteAllByIdUsuario(UUID idUsuario);
=======
    List<Tarefa> findAllByIdUsuario(UUID idUsuario);
>>>>>>> dev
}
