package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.tarefa.application.api.NovaPosicaoDaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

import java.util.List;
import java.util.UUID;
public interface TarefaService {
    TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest);
    Tarefa detalhaTarefa(String usuario, UUID idTarefa);
    void mudaOrdemDaTarefa(String emailDoUsuario, UUID idDaTarefa, NovaPosicaoDaTarefaRequest novaPosicaoDaTarefaRequest);
    void deletaTarefasConcluidas(String usuario, UUID idUsuario);
    List<TarefaDetalhadoResponse> buscaTodasSuasTarefa(String usuario, UUID idUsuario);
}
