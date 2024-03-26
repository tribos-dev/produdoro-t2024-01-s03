package dev.wakandaacademy.produdoro.tarefa.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.wakandaacademy.produdoro.tarefa.application.api.NovaPosicaoDaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

public interface TarefaRepository {
	Tarefa salva(Tarefa tarefa);

	Optional<Tarefa> buscaTarefaPorId(UUID idTarefa);

	List<Tarefa> buscaTodasAsTarefasDoUsuario(UUID idUsuario);

	void salvaVariasTarefas(List<Tarefa> tarefasComNovasPosicoes);

	int contarTarefasDoUsuario(UUID idUsuario);

	void defineNovaPosicaoDaTarefa(Tarefa tarefa, List<Tarefa> tarefas,
			NovaPosicaoDaTarefaRequest novaPosicaoDaTarefaRequest);

	void deletaTodasTarefas(UUID idUsuario);

	void deletaTarefaPorId(Tarefa tarefa);

	List<Tarefa> buscaTarefasConcluidasDoUsuario();

	void deletaTodasAsTarefasConcluidas(List<Tarefa> tarefasConcluidas);

	List<Tarefa> buscaTodasSuasTarefa(UUID IdUsuario);

}
