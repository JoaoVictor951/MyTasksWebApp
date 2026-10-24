package com.mytasks.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.mytasks.dao.TarefaDAO;
import com.mytasks.exception.RegraNegocioException;
import com.mytasks.model.Categoria;
import com.mytasks.model.StatusTarefa;
import com.mytasks.model.Tarefa;
import com.mytasks.util.DataUtil;
import com.mytasks.validator.TarefaValidator;

@Service
public class TarefaService {

    private final TarefaDAO tarefaDAO;
    private final TarefaValidator validator;

    public TarefaService(TarefaDAO tarefaDAO, TarefaValidator validator) {
        this.tarefaDAO = tarefaDAO;
        this.validator = validator;
    }

    public Tarefa criar(int usuarioId, String titulo, String descricao,
                        String categoria, String status, String dataTexto)
            throws RegraNegocioException {
        validator.validar(titulo, descricao, categoria, status, dataTexto);

        Tarefa tarefa = new Tarefa(
                titulo.trim(),
                descricao.trim(),
                Categoria.deTexto(categoria),
                StatusTarefa.deTexto(status),
                DataUtil.converter(dataTexto),
                usuarioId);
        tarefaDAO.inserir(tarefa);
        return tarefa;
    }

    public void editar(int usuarioId, int idTarefa, String titulo, String descricao,
                       String categoria, String status, String dataTexto)
            throws RegraNegocioException {
        validator.validar(titulo, descricao, categoria, status, dataTexto);

        Tarefa tarefa = buscarDoUsuario(usuarioId, idTarefa);
        tarefa.setTitulo(titulo.trim());
        tarefa.setDescricao(descricao.trim());
        tarefa.setCategoria(Categoria.deTexto(categoria));
        tarefa.setStatus(StatusTarefa.deTexto(status));
        tarefa.setDataEntrega(DataUtil.converter(dataTexto));
        tarefaDAO.atualizar(tarefa);
    }

    public void excluir(int usuarioId, int idTarefa) throws RegraNegocioException {
        buscarDoUsuario(usuarioId, idTarefa);
        tarefaDAO.excluir(idTarefa);
    }

    // filtroStatus pode ser "Todas" (ou vazio) para trazer tudo.
    public List<Tarefa> listar(int usuarioId, String filtroStatus) throws RegraNegocioException {
        List<Tarefa> todas = tarefaDAO.listarPorUsuario(usuarioId);

        if (filtroStatus == null || filtroStatus.trim().isEmpty()
                || filtroStatus.equalsIgnoreCase("Todas")) {
            return todas;
        }

        StatusTarefa status = StatusTarefa.deTexto(filtroStatus);
        if (status == null) {
            throw new RegraNegocioException("Status inválido!");
        }

        List<Tarefa> filtradas = new ArrayList<>();
        for (Tarefa t : todas) {
            if (t.getStatus() == status) {
                filtradas.add(t);
            }
        }
        return filtradas;
    }

    // Garante que a tarefa existe e pertence ao usuário que está pedindo.
    private Tarefa buscarDoUsuario(int usuarioId, int idTarefa) throws RegraNegocioException {
        Tarefa tarefa = tarefaDAO.buscarPorId(idTarefa);
        if (tarefa == null || tarefa.getUsuarioId() != usuarioId) {
            throw new RegraNegocioException("Tarefa não encontrada!");
        }
        return tarefa;
    }
}
