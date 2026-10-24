package com.mytasks.dao.memoria;

import java.util.ArrayList;
import java.util.List;
import com.mytasks.dao.UsuarioDAO;
import com.mytasks.model.Usuario;

// Versão em memória do DAO. Serve para testar as regras do sistema sem precisar do MySQL.
public class UsuarioDAOMemoria implements UsuarioDAO {

    private final List<Usuario> usuarios = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public void inserir(Usuario usuario) {
        usuario.setId(proximoId++);
        usuarios.add(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void excluir(int id) {
        usuarios.removeIf(u -> u.getId() == id);
    }
}
