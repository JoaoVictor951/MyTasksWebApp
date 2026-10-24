package com.mytasks.exception;

// Pedido feito sem estar logado.
public class NaoAutenticadoException extends RuntimeException {

    public NaoAutenticadoException() {
        super("Faça login para continuar.");
    }
}
