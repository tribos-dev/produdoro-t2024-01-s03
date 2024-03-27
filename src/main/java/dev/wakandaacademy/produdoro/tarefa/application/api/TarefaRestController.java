package dev.wakandaacademy.produdoro.tarefa.application.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.wakandaacademy.produdoro.config.security.service.TokenService;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.service.TarefaService;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TarefaRestController implements TarefaAPI {
	private final TarefaService tarefaService;
	private final TokenService tokenService;

	public TarefaIdResponse postNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia]  TarefaRestController - postNovaTarefa  ");
		TarefaIdResponse tarefaCriada = tarefaService.criaNovaTarefa(tarefaRequest);
		log.info("[finaliza]  TarefaRestController - postNovaTarefa");
		return tarefaCriada;
	}

	@Override
	public TarefaDetalhadoResponse detalhaTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - detalhaTarefa");
		String usuario = getUsuarioByToken(token);
		Tarefa tarefa = tarefaService.detalhaTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - detalhaTarefa");
		return new TarefaDetalhadoResponse(tarefa);
	}

	@Override
	public void ativaTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - tarefaAtiva");
		String emailUsuario = getUsuarioByToken(token);
		tarefaService.ativaTarefa(idTarefa, emailUsuario);
		log.info("[finaliza] TarefaRestController - tarefaAtiva");
	}

	@Override
	public void editaTarefa(String token, UUID idTarefa, EditaTarefaRequest tarefaRequest) {
		log.info("[inicia] TarefaRestController - editaTarefa");
		String emailUsuario = getUsuarioByToken(token);
		tarefaService.editaTarefa(emailUsuario, idTarefa, tarefaRequest);
		log.info("[finaliza] TarefaRestController - editaTarefa");
	}

	@Override
	public void deletaTodasTarefas(String token, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - deletaTodasTarefas");
		String emailUsuario = getUsuarioByToken(token);
		tarefaService.deletaTodasTarefas(emailUsuario, idUsuario);
		log.info("[finaliza] TarefaRestController - deletaTodasTarefas");
	}

	@Override
	public void deletaTarefa(String token, UUID idTarefa) {
		log.info("[inicia]  TarefaRestController - deletaTarefa  ");
		String usuario = getUsuarioByToken(token);
		tarefaService.deletaTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - deletaTarefa");
	}

	@Override
	public void deletaTarefasConcluidas(String token, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - deletaTrefasConcluidas");
		String usuario = getUsuarioByToken(token);
		tarefaService.deletaTarefasConcluidas(usuario, idUsuario);
		log.info("[finaliza] TarefaRestController - deletaTrefasConcluidas");
	}

	@Override
	public void patchIncrementaPomodoro(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - patchIncrementaPomodoro");
		String usuario = getUsuarioByToken(token);
		tarefaService.patchIncrementaPomodoro(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - patchIncrementaPomodoro");
	}

	@Override
	public void mudaOrdemDaTarefa(String token, UUID idTarefa, NovaPosicaoDaTarefaRequest novaPosicaoDaTarefaRequest) {
		log.info("[inicia] TarefaRestController - mudaOrdemDaTarefa");
		String emailDoUsuario = getUsuarioByToken(token);
		tarefaService.mudaOrdemDaTarefa(emailDoUsuario, idTarefa, novaPosicaoDaTarefaRequest);
		log.info("[emailDoUsuario] {}", emailDoUsuario);
		log.info("[finaliza] UsuarioController - mudaOrdemDaTarefa");
	}

	@Override
	public void concluiTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - concluiTarefa");
		String usuario = getUsuarioByToken(token);
		tarefaService.concluiTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - concluiTarefa");
	}

	@Override
	public List<TarefaDetalhadoResponse> buscaTodasSuasTarefa(String token, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - buscaTodasSuasTarefa");
		String usuario = getUsuarioByToken(token);
		List<TarefaDetalhadoResponse> tarefaDetalhada = tarefaService.buscaTodasSuasTarefa(usuario, idUsuario);
		log.info("[finaliza] TarefaRestController - detalhaTarefa");
		return tarefaDetalhada;
	}

    public void deletaTrefasConcluidas(String token, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - deletaTrefasConcluidas");
		String usuario = getUsuarioByToken(token);
		tarefaService.deletaTarefasConcluidas(usuario,idUsuario);
		log.info("[finaliza] TarefaRestController - deletaTrefasConcluidas");
    }

	private String getUsuarioByToken(String token) {
		log.debug("[token] {}", token);
		String usuario = tokenService.getUsuarioByBearerToken(token)
				.orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, token));
		log.info("[usuario] {}", usuario);
		return usuario;
	}
}
