package dev.wakandaacademy.produdoro.usuario.application.service;

import dev.wakandaacademy.produdoro.DataHelper;
<<<<<<< HEAD
import dev.wakandaacademy.produdoro.autenticacao.domain.Token;
import dev.wakandaacademy.produdoro.config.security.service.TokenService;
=======
>>>>>>> dev
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.StatusUsuario;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
<<<<<<< HEAD
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {
    @InjectMocks
    UsuarioApplicationService usuarioApplicationService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Test
    void deveMudarStatusParaPausaCurta() {
        Usuario usuario = DataHelper.createUsuario();
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
        usuarioApplicationService.mudaStatusPausaCurta(usuario.getIdUsuario(), usuario.getEmail());
        verify(usuarioRepository, times(1)).buscaUsuarioPorId(any());
        verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(any());
        verify(usuarioRepository, times(1)).salva(any());
        assertEquals(StatusUsuario.PAUSA_CURTA, usuario.getStatus());
    }

    @Test
    void naoDeveMudarStatusParaPausaCurta() {
        Usuario usuario = DataHelper.createUsuario();
        UUID idUsuario = UUID.fromString("b92ee6fa-9ae9-45ac-afe0-fb8e4460d839");
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
        APIException e = assertThrows(APIException.class,
                () -> usuarioApplicationService.mudaStatusPausaCurta(idUsuario, usuario.getEmail()));
        assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusException());
    }
=======
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {
    //	@Autowired
    @InjectMocks
    UsuarioApplicationService usuarioApplicationService;
    //	@MockBean
    @Mock
    UsuarioRepository usuarioRepository;

    @Test
    void mudaStatusParaFocoTest() {
        //cenario
        Usuario usuario = DataHelper.createUsuario();
        when(usuarioRepository.salva(any())).thenReturn(usuario);
        when(usuarioRepository.buscaUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);
        //acao
        usuarioApplicationService.mudaStatusParaFoco(usuario.getEmail(), usuario.getIdUsuario());
        //verificacao
        verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(usuario.getEmail());
        verify(usuarioRepository, times(1)).buscaUsuarioPorId(usuario.getIdUsuario());
        verify(usuarioRepository,  times(1)).salva(usuario);
        assertEquals(StatusUsuario.FOCO, usuario.getStatus());
    }
    @Test
    void validaUsuarioTest() {
        //cenario
        Usuario usuario = DataHelper.createUsuario();
        UUID idUsuarioInvalido = UUID.randomUUID(); // ID inválido de exemplo
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        //acao
        APIException exception = assertThrows(APIException.class,
                () -> usuarioApplicationService.mudaStatusParaFoco("emailinvalido@email.com", idUsuarioInvalido));
        //verificacao
        assertEquals("Credencial de autenticacao nao e valida", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusException());
    }
    @Test
    void validaSeUsuarioJaEstaEmFocoTest() {
        //cenario
        Usuario usuario = DataHelper.createUsuario();
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        usuarioApplicationService.mudaStatusParaFoco(usuario.getEmail(), usuario.getIdUsuario());
        //acao
        APIException exception = assertThrows(APIException.class, usuario::validaSeUsuarioJaEstaEmFoco);
        //verificacao
        assertEquals("Usuário já esta em FOCO!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
    }
	@Test
	void UsuarioMudaStatusPausaLongaSucesso() {
		Usuario usuario = DataHelper.createUsuario();
		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
		usuarioApplicationService.mudaStatusPausaLonga(usuario.getEmail(), usuario.getIdUsuario());
		verify(usuarioRepository, times(1)).salva(any());
		
	}
	@Test
	void UsuarioMudaStatusPausaLongaFalha() {
		Usuario usuario = DataHelper.createUsuario();
		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
		APIException ex = assertThrows(APIException.class,
				() -> usuarioApplicationService.mudaStatusPausaLonga(usuario.getEmail(), UUID.randomUUID()));
		assertEquals(APIException.class, ex.getClass());
		assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusException());
		assertEquals("Credencial de autenticacao nao e valida", ex.getMessage());
	}
>>>>>>> dev
}