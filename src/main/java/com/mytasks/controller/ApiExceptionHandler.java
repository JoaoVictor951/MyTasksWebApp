package com.mytasks.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.mytasks.exception.DaoException;
import com.mytasks.exception.NaoAutenticadoException;
import com.mytasks.exception.RegraNegocioException;

// Transforma as exceções em respostas JSON com o campo "mensagem".
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> regraDeNegocio(RegraNegocioException e) {
        return Map.of("mensagem", e.getMessage());
    }

    @ExceptionHandler(NaoAutenticadoException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> naoAutenticado(NaoAutenticadoException e) {
        return Map.of("mensagem", e.getMessage());
    }

    @ExceptionHandler(DaoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> erroDeBanco(DaoException e) {
        e.printStackTrace();
        return Map.of("mensagem", "Erro ao acessar o banco de dados.");
    }
}
