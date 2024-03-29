package dev.wakandaacademy.produdoro.usuario.application.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/public/v1/usuario")
public interface UsuarioAPI {
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	UsuarioCriadoResponse postNovoUsuario(@RequestBody @Valid UsuarioNovoRequest usuarioNovo);

	@GetMapping(value = "/{idUsuario}")
	@ResponseStatus(code = HttpStatus.OK)
	UsuarioCriadoResponse buscaUsuarioPorId(@PathVariable UUID idUsuario);

	@PatchMapping(value = "/pausaCurta/{idUsuario}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void mudaStatusPausaCurta(@RequestHeader (name = "Authorization", required = true) String token,
							  @PathVariable UUID idUsuario);
	@PostMapping(value = "/{idUsuario}/foco")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void mudaStatusParaFoco(@RequestHeader(name = "Authorization",required = true) String token,
							@PathVariable UUID idUsuario);
	@PatchMapping(value = "/{idUsuario}/pausaLonga")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void mudaStatusPausaLonga(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable UUID idUsuario);
	
	
	
}