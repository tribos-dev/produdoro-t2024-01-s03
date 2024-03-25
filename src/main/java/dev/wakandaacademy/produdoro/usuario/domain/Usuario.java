package dev.wakandaacademy.produdoro.usuario.domain;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.pomodoro.domain.ConfiguracaoPadrao;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioNovoRequest;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Email;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@Document(collection = "Usuario")
@Log4j2
public class Usuario {
    @Id
    private UUID idUsuario;
    @Email
    @Indexed(unique = true)
    private String email;
    private ConfiguracaoUsuario configuracao;
    @Builder.Default
    private StatusUsuario status = StatusUsuario.FOCO;
    @Builder.Default
    private Integer quantidadePomodorosPausaCurta = 0;

    public Usuario(UsuarioNovoRequest usuarioNovo, ConfiguracaoPadrao configuracaoPadrao) {
        this.idUsuario = UUID.randomUUID();
        this.email = usuarioNovo.getEmail();
        this.status = StatusUsuario.FOCO;
        this.configuracao = new ConfiguracaoUsuario(configuracaoPadrao);
    }

    public void validaSeUsuarioJaEstaEmFoco() {
        log.info("[inicia] Usuario - validaSeUsuarioJaEstaEmFoco");
        if (this.status.equals(StatusUsuario.FOCO)) {
            log.info("[finaliza] APIException - validaSeUsuarioJaEstaEmFoco");
            throw APIException.build(HttpStatus.BAD_REQUEST, "Usuário já esta em FOCO!");
        }
        log.info("[Finaliza] Usuario - validaSeUsuarioJaEstaEmFoco");
    }

    public void mudaStatusParaFoco(UUID idUsuario) {
        log.info("[inicia] Usuario - mudaStatusParaFoco");
        validaUsuario(idUsuario);
        validaSeUsuarioJaEstaEmFoco();
        this.status = StatusUsuario.FOCO;
        log.info("[finaliza] Usuario - mudaStatusParaFoco");
    }

    public void validaUsuario(UUID idUsuario) {
        log.info("[inicia] Usuario - validaUsuario");
        if (!this.idUsuario.equals(idUsuario)) {
            log.info("[finaliza] APIException - validaUsuario");
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Credencial de autenticação não é válida!");
        }
        log.info("[finaliza] Usuario - validaUsuario");
    }


    public void mudaStatusParaPausaLonga() {
        log.info("[inicia] Usuario - mudaStatusParaPausaLonga");
        this.status = StatusUsuario.PAUSA_LONGA;
        log.info("[finaliza] Usuario - mudaStatusParaPausaLonga");

    }
}

