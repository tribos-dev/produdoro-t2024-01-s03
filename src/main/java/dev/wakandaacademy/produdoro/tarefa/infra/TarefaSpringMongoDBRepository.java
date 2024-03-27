package dev.wakandaacademy.produdoro.tarefa.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.wakandaacademy.produdoro.tarefa.domain.StatusTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

public interface TarefaSpringMongoDBRepository extends MongoRepository<Tarefa, UUID> {
	Optional<Tarefa> findByIdTarefa(UUID idTarefa);

	List<Tarefa> findAllByIdUsuarioOrderByPosicaoAsc(UUID idUsuario);

	int countByIdUsuario(UUID idUsuario);

	List<Tarefa> findAllByStatus(StatusTarefa statusTarefa);

	void deleteAllByIdUsuario(UUID idUsuario);

	List<Tarefa> findAllByIdUsuario(UUID idUsuario);

}
