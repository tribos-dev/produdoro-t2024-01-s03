package dev.wakandaacademy.produdoro.tarefa.domain;

import java.util.UUID;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.EditaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Log4j2
@Document(collection = "Tarefa")
public class Tarefa {
    @Id
    private UUID idTarefa;
    @NotBlank
    private String descricao;
    @Indexed
    private UUID idUsuario;
    @Indexed
    private UUID idArea;
    @Indexed
    private UUID idProjeto;
    private StatusTarefa status;
    private StatusAtivacaoTarefa statusAtivacao;
    private int contagemPomodoro;

    public Tarefa(TarefaRequest tarefaRequest) {
        this.idTarefa = UUID.randomUUID();
        this.idUsuario = tarefaRequest.getIdUsuario();
        this.descricao = tarefaRequest.getDescricao();
        this.idArea = tarefaRequest.getIdArea();
        this.idProjeto = tarefaRequest.getIdProjeto();
        this.status = StatusTarefa.A_FAZER;
        this.statusAtivacao = StatusAtivacaoTarefa.INATIVA;
        this.contagemPomodoro = 1;
    }

	public void altera(EditaTarefaRequest tarefaRequest) {
		this.descricao = tarefaRequest.getDescricao();
	}

    public void pertenceAoUsuario(Usuario usuarioPorEmail) {
        log.info("[inicia] Tarefa - pertenceAoUsuario");
        if (!this.idUsuario.equals(usuarioPorEmail.getIdUsuario())) {
            log.info("[Finaliza] APIException - pertenceAoUsuario");
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não é dono da Tarefa solicitada!");
        }
        log.info("[Finaliza] Tarefa - pertenceAoUsuario");
    }

    public void incrementaPomodoro() {
        log.info("[inicia] Tarefa - incrementaPomodoro");
        this.contagemPomodoro++;
        log.info("[finaliza] Tarefa - incrementaPomodoro");
    }

    public void concluiTarefa() {
        log.info("[inicia] Tarefa - concluiTarefa");
        this.status = StatusTarefa.CONCLUIDA;
        log.info("[finaliza] Tarefa - concluiTarefa");
    }
}
