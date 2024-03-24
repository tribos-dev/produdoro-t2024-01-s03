package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.NovaPosicaoDaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class TarefaApplicationService implements TarefaService {
    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;


    @Override
    public TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest) {
        log.info("[inicia] TarefaApplicationService - criaNovaTarefa");
        int posicaoDaNovaTarefa = tarefaRepository.contarTarefasDoUsuario(tarefaRequest.getIdUsuario());
        Tarefa tarefaCriada = tarefaRepository.salva(new Tarefa(tarefaRequest, posicaoDaNovaTarefa));
        log.info("[finaliza] TarefaApplicationService - criaNovaTarefa");
        return TarefaIdResponse.builder().idTarefa(tarefaCriada.getIdTarefa()).build();
    }
    @Override
    public Tarefa detalhaTarefa(String usuario, UUID idTarefa) {
        log.info("[inicia] TarefaApplicationService - detalhaTarefa");
        Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
        log.info("[usuarioPorEmail] {}", usuarioPorEmail);
        Tarefa tarefa =
                tarefaRepository.buscaTarefaPorId(idTarefa).orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tarefa não encontrada!"));
        tarefa.pertenceAoUsuario(usuarioPorEmail);
        log.info("[finaliza] TarefaApplicationService - detalhaTarefa");
        return tarefa;
    }
    @Override
    public List<TarefaDetalhadoResponse> buscaTodasSuasTarefa(String usuario, UUID idUsuario) {
        log.info("[inicial] - TarefaApplicationService - buscaTodasSuasTarefa");
        validaUsuario(usuario, idUsuario);
        List<Tarefa> tarefaList = tarefaRepository.buscaTodasSuasTarefa(idUsuario);
        log.info("[finaliza] - TarefaApplicationService - buscaTodasSuasTarefa");
        return TarefaDetalhadoResponse.converte(tarefaList);
    }

    private void validaUsuario(String usuario, UUID idUsuario) {
        Usuario usuarioValidado = usuarioRepository.buscaUsuarioPorEmail(usuario);
        usuarioRepository.buscaUsuarioPorId(idUsuario);
        usuarioValidado.validaUsuario(idUsuario);
    }

    @Override
    public void mudaOrdemDaTarefa(String emailDoUsuario, UUID idDaTarefa, NovaPosicaoDaTarefaRequest novaPosicao) {
        log.info("[inicia] TarefaApplicationService - mudaOrdemDaTarefa");
        Tarefa tarefa = detalhaTarefa(emailDoUsuario, idDaTarefa);
        List<Tarefa> tarefas = tarefaRepository.buscaTodasAsTarefasDoUsuario(tarefa.getIdUsuario());
        tarefaRepository.defineNovaPosicaoDaTarefa(tarefa, tarefas, novaPosicao);
        log.info("[inicia] TarefaApplicationService - mudaOrdemDaTarefa");
    }

    private void definiNovaPosicaoDaTarefa(List<Tarefa> tarefas, Tarefa tarefa, Integer novaPosicao){
        int posicaoAntiga = tarefa.getPosicao();
        int tamanhoDaLista = tarefas.size();

        if (novaPosicao >= tamanhoDaLista)
            throw APIException.build(HttpStatus.BAD_REQUEST, "A posição da tarefa não pode ser igual, nem maior que a quantidade total de tarefas");
        if (Objects.equals(novaPosicao, posicaoAntiga))
            throw APIException.build(HttpStatus.CONFLICT, "A posição enviada é igual à já presente, insira uma nova.");

        tarefa.setPosicao(novaPosicao);
        tarefaRepository.salva(tarefa);
        List<Tarefa> tarefasComNovasPosicoes = new ArrayList<>();

        if (novaPosicao < posicaoAntiga) {
            for (int i = novaPosicao; i < posicaoAntiga; i++) {
                tarefas.get(i).incrementaPosicao(tamanhoDaLista);
                tarefasComNovasPosicoes.add(tarefas.get(i));
            }
        }
        else {
            for (int i = posicaoAntiga + 1; i <= novaPosicao; i++) {
                tarefas.get(i).decrementaPosicao();
                tarefasComNovasPosicoes.add(tarefas.get(i));
            }
        }
        tarefaRepository.salvaVariasTarefas(tarefasComNovasPosicoes);
    }

}
