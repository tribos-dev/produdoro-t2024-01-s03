package dev.wakandaacademy.produdoro.tarefa.application.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/tarefa")
public interface TarefaAPI {
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    TarefaIdResponse postNovaTarefa(@RequestBody @Valid TarefaRequest tarefaRequest);
    @GetMapping("/{idTarefa}")
    @ResponseStatus(code = HttpStatus.OK)
    TarefaDetalhadoResponse detalhaTarefa(@RequestHeader(name = "Authorization",required = true) String token, 
    		@PathVariable UUID idTarefa);


    @GetMapping("/listaTarefas/{idUsuario}")
    @ResponseStatus(code = HttpStatus.OK)
    List<TarefaDetalhadoResponse> buscaTodasSuasTarefa(@RequestHeader(name = "Authorization",required = true) String token, @PathVariable UUID idUsuario);

    @PostMapping("/mudar-ordem/{idTarefa}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    void mudaOrdemDaTarefa(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable UUID idTarefa,
            @RequestBody @Valid NovaPosicaoDaTarefaRequest novaPosicaoDaTarefaRequest
    );

    @DeleteMapping("/{idUsuario}/deletaStatusConcluidas")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deletaTrefasConcluidas(@RequestHeader(name = "Authorization",required = true) String token,
                                          @PathVariable UUID idUsuario);
}

