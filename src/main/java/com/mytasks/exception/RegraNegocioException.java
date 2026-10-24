package com.mytasks.exception;

// Erro causado por uma regra do sistema (campo vazio, senha errada, etc).
// A mensagem pode ser mostrada direto para o usuário.
public class RegraNegocioException extends Exception {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
