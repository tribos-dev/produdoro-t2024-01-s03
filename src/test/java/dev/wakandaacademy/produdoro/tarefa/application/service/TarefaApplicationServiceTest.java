package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        when(tarefaRepository.salva(any())).thenReturn(new Tarefa(request));

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
}
