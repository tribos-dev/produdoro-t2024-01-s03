package dev.wakandaacademy.produdoro.tarefa.application.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Value
public class NovaPosicaoDaTarefaRequest {
    @PositiveOrZero @NotNull Integer nova_posicao;

    @JsonCreator
    public NovaPosicaoDaTarefaRequest(@JsonProperty("nova_posicao") Integer nova_posicao) {
        this.nova_posicao = nova_posicao;
    }
}
