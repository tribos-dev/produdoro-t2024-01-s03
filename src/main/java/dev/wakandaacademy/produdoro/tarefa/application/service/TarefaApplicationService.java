package dev.wakandaacademy.produdoro.tarefa.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.EditaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.NovaPosicaoDaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TarefaApplicationService implements TarefaService {
	private final TarefaRepository tarefaRepository;
	private final UsuarioRepository usuarioRepository;

	@Override
	public TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia] TarefaApplicationService - criaNovaTarefa");
		int posicaoDaNovaTarefa = tarefaRepository.contarTarefasDoUsuario(tarefaRequest.getIdUsuario());
		Tarefa tarefaCriada = tarefaRepository.salva(new Tarefa(tarefaRequest, posicaoDaNovaTarefa));
		log.info("[finaliza] TarefaApplicationService - criaNovaTarefa");
		return TarefaIdResponse.builder().idTarefa(tarefaCriada.getIdTarefa()).build();
	}

	@Override
	public Tarefa detalhaTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - detalhaTarefa");
		Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
		log.info("[usuarioPorEmail] {}", usuarioPorEmail);
		Tarefa tarefa = tarefaRepository.buscaTarefaPorId(idTarefa)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tarefa não encontrada!"));
		tarefa.pertenceAoUsuario(usuarioPorEmail);
		log.info("[finaliza] TarefaApplicationService - detalhaTarefa");
		return tarefa;
	}

	@Override
	public void ativaTarefa(UUID idTarefa, String emailUsuario) {
		log.info("[inicia] TarefaApplicationService - ativaTarefa");
		Tarefa tarefa = detalhaTarefa(emailUsuario, idTarefa);
		Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(emailUsuario);
		tarefaRepository.desativaTarefas(usuario.getIdUsuario());
		tarefa.ativaTarefa();
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - ativaTarefa");
	}

	@Override
	public void editaTarefa(String emailUsuario, UUID idTarefa, EditaTarefaRequest tarefaRequest) {
		log.info("[inicia] TarefaApplicationService - editaTarefa");
		Tarefa tarefa = detalhaTarefa(emailUsuario, idTarefa);
		tarefa.altera(tarefaRequest);
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - editaTarefa");
	}

	@Override
	public void deletaTodasTarefas(String emailUsuario, UUID idUsuario) {
		log.info("[inicia] TarefaApplicationService - deletaTodasTarefas");
		Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(emailUsuario);
		usuarioRepository.buscaUsuarioPorId(idUsuario);
		usuario.validaUsuario(idUsuario);
		tarefaRepository.deletaTodasTarefas(idUsuario);
		log.info("[finaliza] TarefaApplicationService - deletaTodasTarefas");
	}

	@Override
	public void concluiTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - concluiTarefa");
		Tarefa tarefa = detalhaTarefa(usuario, idTarefa);
		tarefa.concluiTarefa();
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - concluiTarefa");
	}

	@Override
	public void deletaTarefasConcluidas(String usuario, UUID idUsuario) {
		log.info("[inicia] TarefaApplicationService - deletaTarefasConcluidas");
		validaUsuario(usuario, idUsuario);
		List<Tarefa> tarefasConcluidas = tarefaRepository.buscaTarefasConcluidasDoUsuario();
		tarefaRepository.deletaTodasAsTarefasConcluidas(tarefasConcluidas);
		reordenaTarefasAposDeletarTarefasConcluidas(idUsuario);
		log.info("[finaliza] TarefaApplicationService - deletaTarefasConcluidas");
	}

	@Override
	public List<TarefaDetalhadoResponse> buscaTodasSuasTarefa(String usuario, UUID idUsuario) {
		log.info("[inicial] - TarefaApplicationService - buscaTodasSuasTarefa");
		validaUsuario(usuario, idUsuario);
		List<Tarefa> tarefaList = tarefaRepository.buscaTodasSuasTarefa(idUsuario);
		log.info("[finaliza] - TarefaApplicationService - buscaTodasSuasTarefa");
		return TarefaDetalhadoResponse.converte(tarefaList);
	}

	@Override
	public void patchIncrementaPomodoro(String usuario, UUID idTarefa) {
		log.info("[inicial] - TarefaApplicationService - patchIncrementaPomodoro");
		Tarefa tarefa = detalhaTarefa(usuario, idTarefa);
		tarefa.incrementaPomodoro();
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] - TarefaApplicationService - patchIncrementaPomodoro");
	}

	private void validaUsuario(String usuario, UUID idUsuario) {
		log.info("[Inicia] - TarefaApplicationService - validaUsuario");
		Usuario usuarioValidado = usuarioRepository.buscaUsuarioPorEmail(usuario);
		log.info("[Usuario] - {}", usuarioValidado);
		usuarioRepository.buscaUsuarioPorId(idUsuario);
		usuarioValidado.validaUsuario(idUsuario);
		log.info("[finaliza] - TarefaApplicationService - validaUsuario");
	}

	@Override
	public void mudaOrdemDaTarefa(String emailDoUsuario, UUID idDaTarefa, NovaPosicaoDaTarefaRequest novaPosicao) {
		log.info("[inicia] TarefaApplicationService - mudaOrdemDaTarefa");
		Tarefa tarefa = detalhaTarefa(emailDoUsuario, idDaTarefa);
		List<Tarefa> tarefas = tarefaRepository.buscaTodasAsTarefasDoUsuario(tarefa.getIdUsuario());
		tarefaRepository.defineNovaPosicaoDaTarefa(tarefa, tarefas, novaPosicao);
		log.info("[finaliza] TarefaApplicationService - mudaOrdemDaTarefa");
	}

	@Override
	public void reordenaTarefasAposDeletarUmaTarefaEspecifica(String emailDoUsuario, Tarefa tarefaDeletada) {
		log.info("[inicia] TarefaApplicationService - reordenaTarefasAposDeletarUmaTarefaEspecifica");
		Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(emailDoUsuario);
		List<Tarefa> tarefas = tarefaRepository.buscaTodasAsTarefasDoUsuario(usuario.getIdUsuario());
		for (int i = tarefaDeletada.getPosicao(); i < tarefas.size(); i++)
			tarefas.get(i).decrementaPosicao();
		tarefaRepository.salvaVariasTarefas(tarefas);
		log.info("[finaliza] TarefaApplicationService - reordenaTarefasAposDeletarUmaTarefaEspecifica");
	}

	@Override
	public void reordenaTarefasAposDeletarTarefasConcluidas(UUID idUsuario) {
		log.info("[inicia] TarefaApplicationService - reordenaTarefasAposDeletarTarefasConcluidas");
		Usuario usuario = usuarioRepository.buscaUsuarioPorId(idUsuario);
		List<Tarefa> tarefas = tarefaRepository.buscaTodasAsTarefasDoUsuario(usuario.getIdUsuario());
		for (int i = 0; i < tarefas.size(); i++)
			tarefas.get(i).setPosicao(i);
		tarefaRepository.salvaVariasTarefas(tarefas);
		log.info("[finaliza] TarefaApplicationService - reordenaTarefasAposDeletarTarefasConcluidas");
	}

	@Override
	public void deletaTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - deletaTarefa");
		Tarefa tarefaASerDeletada = detalhaTarefa(usuario, idTarefa);
		tarefaRepository.deletaTarefaPorId(tarefaASerDeletada);
		reordenaTarefasAposDeletarUmaTarefaEspecifica(usuario, tarefaASerDeletada);
		log.info("[finaliza] TarefaApplicationService - deletaTarefa");
	}
}
