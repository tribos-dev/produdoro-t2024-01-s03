package dev.wakandaacademy.produdoro.tarefa.application.api;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Value
public class NovaPosicaoDaTarefaRequest {
    @NotBlank @Positive int novaPosicao;
}
