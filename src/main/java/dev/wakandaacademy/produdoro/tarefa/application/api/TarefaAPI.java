package dev.wakandaacademy.produdoro.tarefa.application.api;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tarefa")
public interface TarefaAPI {
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	TarefaIdResponse postNovaTarefa(@RequestBody @Valid TarefaRequest tarefaRequest);

	@GetMapping("/{idTarefa}")
	@ResponseStatus(code = HttpStatus.OK)
	TarefaDetalhadoResponse detalhaTarefa(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable UUID idTarefa);

	@PatchMapping("/incrementaPomodoro/{idTarefa}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void patchIncrementaPomodoro(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable UUID idTarefa);

	@PatchMapping("/concluiTarefa/{idTarefa}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void concluiTarefa(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable UUID idTarefa);

	@GetMapping("/listaTarefas/{idUsuario}")
	@ResponseStatus(code = HttpStatus.OK)
	List<TarefaDetalhadoResponse> buscaTodasSuasTarefa(
			@RequestHeader(name = "Authorization", required = true) String token, @PathVariable UUID idUsuario);

	@PostMapping("/mudar-ordem/{idTarefa}")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	void mudaOrdemDaTarefa(@RequestHeader(name = "Authorization") String token, @PathVariable UUID idTarefa,
			@RequestBody @Valid NovaPosicaoDaTarefaRequest novaPosicaoDaTarefaRequest);

	@DeleteMapping("/{idUsuario}/deletaStatusConcluidas")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void deletaTarefasConcluidas(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable UUID idUsuario);

	@DeleteMapping("/deleta-tarefa/{idTarefa}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void deletaTarefa(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable UUID idTarefa);

	@PatchMapping("/editaTarefa/{idTarefa}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void editaTarefa(@RequestHeader(name = "Authorization", required = true) String token, @PathVariable UUID idTarefa,
			@RequestBody @Valid EditaTarefaRequest tarefaRequest);

	@DeleteMapping("/deletaTodasTarefas/{idUsuario}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void deletaTodasTarefas(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable UUID idUsuario);
}
