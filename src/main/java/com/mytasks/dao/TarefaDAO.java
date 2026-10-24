package com.mytasks.dao;

import java.util.List;
import com.mytasks.model.Tarefa;

public interface TarefaDAO {

    // Grava a tarefa e preenche o id gerado.
    void inserir(Tarefa tarefa);

    void atualizar(Tarefa tarefa);

    void excluir(int id);

    // Retorna null se não encontrar.
    Tarefa buscarPorId(int id);

    List<Tarefa> listarPorUsuario(int usuarioId);
}
