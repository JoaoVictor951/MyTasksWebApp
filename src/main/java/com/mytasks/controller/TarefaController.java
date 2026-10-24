package com.mytasks.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mytasks.dto.Dtos.ResumoResponse;
import com.mytasks.dto.Dtos.TarefaRequest;
import com.mytasks.dto.Dtos.TarefaResponse;
import com.mytasks.exception.RegraNegocioException;
import com.mytasks.model.StatusTarefa;
import com.mytasks.model.Tarefa;
import com.mytasks.service.CalculadoraTarefas;
import com.mytasks.service.TarefaService;
import com.mytasks.util.DataUtil;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private static final int DIAS_PRAZO_PROXIMO = 3;

    private final TarefaService tarefaService;
    private final CalculadoraTarefas calculadora;

    public TarefaController(TarefaService tarefaService, CalculadoraTarefas calculadora) {
        this.tarefaService = tarefaService;
        this.calculadora = calculadora;
    }

    @GetMapping
    public List<TarefaResponse> listar(@RequestParam(required = false) String status, HttpSession session)
            throws RegraNegocioException {
        int usuarioId = SessaoWeb.usuarioId(session);
        LocalDate hoje = LocalDate.now();
        List<TarefaResponse> resposta = new ArrayList<>();
        for (Tarefa t : tarefaService.listar(usuarioId, status)) {
            resposta.add(converter(t, hoje));
        }
        return resposta;
    }

    @GetMapping("/resumo")
    public ResumoResponse resumo(HttpSession session) throws RegraNegocioException {
        List<Tarefa> todas = tarefaService.listar(SessaoWeb.usuarioId(session), "Todas");
        LocalDate hoje = LocalDate.now();
        int atrasadas = 0;
        for (Tarefa t : todas) {
            if (calculadora.estaAtrasada(t, hoje)) {
                atrasadas++;
            }
        }
        return new ResumoResponse(
                todas.size(),
                calculadora.contarPorStatus(todas, StatusTarefa.PENDENTE),
                calculadora.contarPorStatus(todas, StatusTarefa.EM_ANDAMENTO),
                calculadora.contarPorStatus(todas, StatusTarefa.CONCLUIDO),
                atrasadas,
                calculadora.percentualConcluido(todas));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@RequestBody TarefaRequest req, HttpSession session) throws RegraNegocioException {
        Tarefa t = tarefaService.criar(SessaoWeb.usuarioId(session), req.titulo(), req.descricao(),
                req.categoria(), req.status(), req.dataEntrega());
        return converter(t, LocalDate.now());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editar(@PathVariable int id, @RequestBody TarefaRequest req, HttpSession session)
            throws RegraNegocioException {
        tarefaService.editar(SessaoWeb.usuarioId(session), id, req.titulo(), req.descricao(),
                req.categoria(), req.status(), req.dataEntrega());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id, HttpSession session) throws RegraNegocioException {
        tarefaService.excluir(SessaoWeb.usuarioId(session), id);
    }

    private TarefaResponse converter(Tarefa t, LocalDate hoje) {
        return new TarefaResponse(t.getId(), t.getTitulo(), t.getDescricao(),
                t.getCategoria().getDescricao(), t.getStatus().getDescricao(),
                DataUtil.formatar(t.getDataEntrega()),
                calculadora.estaAtrasada(t, hoje),
                calculadora.prazoProximo(t, hoje, DIAS_PRAZO_PROXIMO));
    }
}
