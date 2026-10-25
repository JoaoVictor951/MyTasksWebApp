package com.mytasks.validator;

import org.springframework.stereotype.Component;

import com.mytasks.exception.RegraNegocioException;

@Component
public class UsuarioValidator {

    private static final int TAMANHO_MAXIMO_NOME = 100;
    private static final int TAMANHO_MAXIMO_EMAIL = 150;
    // o BCrypt só usa os 72 primeiros caracteres da senha
    private static final int TAMANHO_MAXIMO_SENHA = 72;

    public void validarCadastro(String nome, String email, String senha, String confirmarSenha)
            throws RegraNegocioException {
        if (vazio(nome) || vazio(email) || vazio(senha) || vazio(confirmarSenha)) {
            throw new RegraNegocioException("Preencha todos os campos!");
        }
        if (nome.trim().length() > TAMANHO_MAXIMO_NOME) {
            throw new RegraNegocioException("O nome pode ter no máximo 100 caracteres!");
        }
        if (email.trim().length() > TAMANHO_MAXIMO_EMAIL) {
            throw new RegraNegocioException("O e-mail pode ter no máximo 150 caracteres!");
        }
        if (senha.length() > TAMANHO_MAXIMO_SENHA) {
            throw new RegraNegocioException("A senha pode ter no máximo 72 caracteres!");
        }
        if (!senha.equals(confirmarSenha)) {
            throw new RegraNegocioException("As senhas precisam ser iguais!");
        }
    }

    public void validarLogin(String email, String senha) throws RegraNegocioException {
        if (vazio(email) || vazio(senha)) {
            throw new RegraNegocioException("Preencha todos os campos!");
        }
        // nenhuma senha cadastrada passa de 72 caracteres
        if (senha.length() > TAMANHO_MAXIMO_SENHA) {
            throw new RegraNegocioException("Email ou senha inválidos!");
        }
    }

    private boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
