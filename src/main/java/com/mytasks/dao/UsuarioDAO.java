package com.mytasks.dao;

import com.mytasks.model.Usuario;

public interface UsuarioDAO {

    // Grava o usuário e preenche o id gerado.
    void inserir(Usuario usuario);

    // Retorna null se não encontrar.
    Usuario buscarPorEmail(String email);

    void excluir(int id);
}
