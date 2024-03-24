package dev.wakandaacademy.produdoro.tarefa.application.repository;

import dev.wakandaacademy.produdoro.tarefa.application.api.NovaPosicaoDaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarefaRepository {
    Tarefa salva(Tarefa tarefa);
    Optional<Tarefa> buscaTarefaPorId(UUID idTarefa);
    List<Tarefa> buscaTodasAsTarefasDoUsuario(UUID idUsuario);
    void salvaVariasTarefas(List<Tarefa> tarefasComNovasPosicoes);
    int contarTarefasDoUsuario(UUID idUsuario);
    void defineNovaPosicaoDaTarefa(Tarefa tarefa, List<Tarefa> tarefas, NovaPosicaoDaTarefaRequest novaPosicaoDaTarefaRequest);
    List<Tarefa> buscaTarefasConcluidasDoUsuario();
    void deletaTodasAsTarefasConcluidas(List<Tarefa> tarefasConcluidas);
    List<Tarefa> buscaTodasSuasTarefa(UUID IdUsuario);
    void deletaTarefaPorId(Tarefa tarefa);
}
