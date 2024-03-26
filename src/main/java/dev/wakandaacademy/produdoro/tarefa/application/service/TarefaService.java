package dev.wakandaacademy.produdoro.tarefa.application.service;

import java.util.List;
import java.util.UUID;

import dev.wakandaacademy.produdoro.tarefa.application.api.EditaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.NovaPosicaoDaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

public interface TarefaService {
	TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest);

	Tarefa detalhaTarefa(String usuario, UUID idTarefa);

	void mudaOrdemDaTarefa(String emailDoUsuario, UUID idDaTarefa,
			NovaPosicaoDaTarefaRequest novaPosicaoDaTarefaRequest);

	void reordenaTarefasAposDeletarUmaTarefaEspecifica(String emailDoUsuario, Tarefa tarefaDeletada);

	void reordenaTarefasAposDeletarTarefasConcluidas(UUID idUsuario);

	List<TarefaDetalhadoResponse> buscaTodasSuasTarefa(String usuario, UUID idUsuario);

	void concluiTarefa(String usuario, UUID idTarefa);

	void deletaTarefa(String usuario, UUID idTarefa);

	void editaTarefa(String emailUsuario, UUID idTarefa, EditaTarefaRequest tarefaRequest);

	void deletaTodasTarefas(String emailUsuario, UUID idUsuario);

	void deletaTarefasConcluidas(String usuario, UUID idUsuario);

	void patchIncrementaPomodoro(String usuario, UUID idTarefa);
}
