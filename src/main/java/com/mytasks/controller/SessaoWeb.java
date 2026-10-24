package com.mytasks.controller;

import jakarta.servlet.http.HttpSession;
import com.mytasks.exception.NaoAutenticadoException;

// Guarda e consulta quem está logado na sessão HTTP (no desktop era a classe SessaoUsuario).
public class SessaoWeb {

    public static final String USUARIO_ID = "usuarioId";
    public static final String USUARIO_NOME = "usuarioNome";

    private SessaoWeb() {
    }

    public static int usuarioId(HttpSession session) {
        Object id = session.getAttribute(USUARIO_ID);
        if (id == null) {
            throw new NaoAutenticadoException();
        }
        return (Integer) id;
    }
}
