package com.mytasks.exception;

// Erro técnico de acesso ao banco.
public class DaoException extends RuntimeException {

    public DaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
