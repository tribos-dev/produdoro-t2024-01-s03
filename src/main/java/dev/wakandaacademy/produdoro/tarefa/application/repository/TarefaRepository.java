package dev.wakandaacademy.produdoro.tarefa.application.repository;

import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarefaRepository {

    Tarefa salva(Tarefa tarefa);
    Optional<Tarefa> buscaTarefaPorId(UUID idTarefa);
    void deletaTodasTarefas(UUID idUsuario);
    void deletaTarefaPorId(Tarefa tarefa);
    List<Tarefa> buscaTarefasConcluidasDoUsuario();
    void deletaTodasAsTarefasConcluidas(List<Tarefa> tarefasConcluidas);
    List<Tarefa> buscaTodasSuasTarefa(UUID IdUsuario);


}
