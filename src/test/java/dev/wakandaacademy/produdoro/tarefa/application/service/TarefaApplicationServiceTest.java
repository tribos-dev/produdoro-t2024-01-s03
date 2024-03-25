package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.*;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;

import lombok.extern.log4j.Log4j2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.anyString;

import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;

import dev.wakandaacademy.produdoro.tarefa.domain.StatusTarefa;

@ExtendWith(MockitoExtension.class)
@Log4j2
class TarefaApplicationServiceTest {

    //	@Autowired
    @InjectMocks
    TarefaApplicationService tarefaApplicationService;

    //	@MockBean
    @Mock
    TarefaRepository tarefaRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Test
    void deveRetornarIdTarefaNovaCriada() {
        TarefaRequest request = getTarefaRequest();
        when(tarefaRepository.salva(any())).thenReturn(new Tarefa(request, 0));

        TarefaIdResponse response = tarefaApplicationService.criaNovaTarefa(request);

        assertNotNull(response);
        assertEquals(TarefaIdResponse.class, response.getClass());
        assertEquals(UUID.class, response.getIdTarefa().getClass());
    }

    @Test
    void deveRetornarListaDeTarefa() {
        String usuario = DataHelper.createUsuario().getEmail();
        UUID idUsuario = DataHelper.createUsuario().getIdUsuario();
        when(tarefaRepository.buscaTodasSuasTarefa(any(UUID.class))).thenReturn(DataHelper.createListTarefa());
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(DataHelper.createUsuario());
        List<TarefaDetalhadoResponse> tarefaDetalhadoResponse = tarefaApplicationService.buscaTodasSuasTarefa(usuario, idUsuario);
        assertNotNull(tarefaDetalhadoResponse);
        assertTrue(tarefaDetalhadoResponse.size() > 1);
    }

    @Test
    void deveRetornarExceptionDaListaDeTarefa() {
        String usuario = DataHelper.createUsuario().getEmail();
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(DataHelper.createUsuario());

        APIException excecao = assertThrows(APIException.class, () -> tarefaApplicationService.buscaTodasSuasTarefa(usuario, UUID.randomUUID()));
        assertNotNull(excecao);
        assertEquals(HttpStatus.UNAUTHORIZED, excecao.getStatusException());
        assertEquals("Credencial de autenticação não é válida!", excecao.getMessage());
    }

    public TarefaRequest getTarefaRequest() {
        TarefaRequest request = new TarefaRequest("tarefa 1", UUID.randomUUID(), null, null, 0);
        return request;
    }
    @Test
    void deletaTarefasConcluidas() {
        Usuario usuario = DataHelper.createUsuario();
        List<Tarefa> tarefas = DataHelper.createListTarefasConcluidas(usuario);
        when(usuarioRepository.buscaUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefasConcluidasDoUsuario()).thenReturn(tarefas);
        List<Tarefa> tarefasConcluidas = tarefaRepository.buscaTarefasConcluidasDoUsuario();
        tarefaApplicationService.deletaTarefasConcluidas(usuario.getEmail(), usuario.getIdUsuario());

        verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(usuario.getEmail());
        verify(usuarioRepository, times(1)).buscaUsuarioPorId(usuario.getIdUsuario());
        verify(tarefaRepository, times(1)).deletaTodasAsTarefasConcluidas(tarefasConcluidas);
    }
    @Test
    void validaUsuarioTest() {
        //cenario
        Usuario usuario = DataHelper.createUsuario();
        UUID idUsuarioInvalido = UUID.randomUUID(); // ID inválido de exemplo
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        //acao
        APIException exception = assertThrows(APIException.class,
                () -> tarefaApplicationService.deletaTarefasConcluidas("emailinvalido@email.com", idUsuarioInvalido));
        //verificacao
        assertEquals("Credencial de autenticação não é válida!", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusException());
    }
    @Test
    public void deveConcluirTarefa() {
    	Usuario usuario = DataHelper.createUsuario();
    	Tarefa tarefa = DataHelper.createTarefa();
    	when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
    	when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));
		tarefaApplicationService.concluiTarefa(usuario.getEmail(), tarefa.getIdTarefa());
		assertEquals(tarefa.getStatus(), StatusTarefa.CONCLUIDA);
    }
    
    @Test
    public void naoConcluiTarefa() {
        Usuario usuario = DataHelper.createUsuario();
        Tarefa tarefa = DataHelper.createTarefa();
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenThrow(APIException.class);
        assertThrows(APIException.class,
                () -> tarefaApplicationService.concluiTarefa("emailInvalido@gmail.com", tarefa.getIdTarefa()));
    }
    @Test
    void testDeletaTarefa(){
        UUID idTarefa = UUID.randomUUID();
        String usuario = "usuario@exemplo.com";
        Usuario usuarioMock = DataHelper.createUsuario();
        Tarefa tarefaMock = DataHelper.createTarefa();

        when(usuarioRepository.buscaUsuarioPorEmail(usuario)).thenReturn(usuarioMock);
        when(tarefaRepository.buscaTarefaPorId(idTarefa)).thenReturn(Optional.of(tarefaMock));
        tarefaApplicationService.deletaTarefa(usuario, idTarefa);

        verify(tarefaRepository, times(1)).deletaTarefaPorId(tarefaMock);
    }

    @Test
    void nãoDeveDeletarTarefa(){
        UUID idTarefa = UUID.fromString("385c48f2-49ab-485b-87b1-02d5de2f7710");
        String usuarioEmail = "exemplo@usuario.com";
        Usuario usuario = DataHelper.createUsuario();
        Tarefa tarefa = DataHelper.createTarefa();

        APIException ex = assertThrows(APIException.class,
                () -> tarefaApplicationService.deletaTarefa(usuario.getEmail(), tarefa.getIdTarefa()));

        assertNotEquals(idTarefa, tarefa.getIdTarefa());
        assertNotEquals(usuarioEmail, usuario.getEmail());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusException());
    }

    @Test
    void mudaOrdemDaTarefaTest() {
        // Given
        List<Tarefa> tarefas = DataHelper.createListTarefa();
        Tarefa tarefa = DataHelper.createTarefa();
        NovaPosicaoDaTarefaRequest novaPosicao = DataHelper.createNovaPosicao(1);
        Usuario usuario = DataHelper.createUsuario();

        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.buscaTodasAsTarefasDoUsuario(tarefa.getIdUsuario())).thenReturn(tarefas);

        // When
        tarefaApplicationService.mudaOrdemDaTarefa(usuario.getEmail(), tarefa.getIdTarefa(), novaPosicao);

        // Then
        verify(tarefaRepository, times(1)).defineNovaPosicaoDaTarefa(tarefa, tarefas, novaPosicao);
    }

    @Test
    void naoDeveMudarOrdemDaTarefa() {
        // Given
        Tarefa tarefaInexistente = DataHelper.createTarefa();
        NovaPosicaoDaTarefaRequest novaPosicao = DataHelper.createNovaPosicao(1);
        Usuario usuario = DataHelper.createUsuario();

        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.empty());

        // When
        assertThrows(APIException.class, () -> tarefaApplicationService.mudaOrdemDaTarefa(usuario.getEmail(), tarefaInexistente.getIdTarefa(), novaPosicao));

        // Then
        verify(tarefaRepository, times(0)).defineNovaPosicaoDaTarefa(any(), any(), any());
    }

}
