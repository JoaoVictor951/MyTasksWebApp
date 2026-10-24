package com.mytasks.validator;

import org.springframework.stereotype.Component;

import com.mytasks.exception.RegraNegocioException;

@Component
public class UsuarioValidator {

    public void validarCadastro(String nome, String email, String senha, String confirmarSenha)
            throws RegraNegocioException {
        if (vazio(nome) || vazio(email) || vazio(senha) || vazio(confirmarSenha)) {
            throw new RegraNegocioException("Preencha todos os campos!");
        }
        if (!senha.equals(confirmarSenha)) {
            throw new RegraNegocioException("As senhas precisam ser iguais!");
        }
    }

    public void validarLogin(String email, String senha) throws RegraNegocioException {
        if (vazio(email) || vazio(senha)) {
            throw new RegraNegocioException("Preencha todos os campos!");
        }
    }

    private boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
