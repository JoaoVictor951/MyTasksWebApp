package com.mytasks.dao.memoria;

import java.util.ArrayList;
import java.util.List;
import com.mytasks.dao.TarefaDAO;
import com.mytasks.model.Tarefa;

public class TarefaDAOMemoria implements TarefaDAO {

    private final List<Tarefa> tarefas = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public void inserir(Tarefa tarefa) {
        tarefa.setId(proximoId++);
        tarefas.add(tarefa);
    }

    @Override
    public void atualizar(Tarefa tarefa) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).getId() == tarefa.getId()) {
                tarefas.set(i, tarefa);
                return;
            }
        }
    }

    @Override
    public void excluir(int id) {
        tarefas.removeIf(t -> t.getId() == id);
    }

    @Override
    public Tarefa buscarPorId(int id) {
        for (Tarefa t : tarefas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Tarefa> listarPorUsuario(int usuarioId) {
        List<Tarefa> resultado = new ArrayList<>();
        for (Tarefa t : tarefas) {
            if (t.getUsuarioId() == usuarioId) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}
