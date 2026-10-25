package com.mytasks.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.mytasks.exception.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TarefaValidatorTest {

    private TarefaValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new TarefaValidator();
    }

    private void esperarErro(String mensagem, String titulo, String descricao,
                             String categoria, String status, String data) {
        try {
            validator.validar(titulo, descricao, categoria, status, data);
            fail("Era esperado o erro: " + mensagem);
        } catch (RegraNegocioException e) {
            assertEquals(mensagem, e.getMessage());
        }
    }

    @Test
    public void dadosValidosNaoLancamErro() throws RegraNegocioException {
        validator.validar("Estudar", "Capitulo 2", "Prova", "Pendente", "20/03/2026");
    }

    @Test
    public void tituloVazio() {
        esperarErro("Preencha todos os campos!", "", "desc", "Prova", "Pendente", "20/03/2026");
    }

    @Test
    public void tituloSoComEspacos() {
        esperarErro("Preencha todos os campos!", "   ", "desc", "Prova", "Pendente", "20/03/2026");
    }

    @Test
    public void descricaoNula() {
        esperarErro("Preencha todos os campos!", "Titulo", null, "Prova", "Pendente", "20/03/2026");
    }

    @Test
    public void dataVazia() {
        esperarErro("Preencha todos os campos!", "Titulo", "desc", "Prova", "Pendente", "");
    }

    @Test
    public void categoriaInvalida() {
        esperarErro("Categoria inválida!", "Titulo", "desc", "Festa", "Pendente", "20/03/2026");
    }

    @Test
    public void statusInvalido() {
        esperarErro("Status inválido!", "Titulo", "desc", "Prova", "Feito", "20/03/2026");
    }

    @Test
    public void dataQueNaoExiste() {
        esperarErro("Data inválida! Use formato válido: 20/03/2026",
                "Titulo", "desc", "Prova", "Pendente", "31/02/2026");
    }

    @Test
    public void dataEmFormatoErrado() {
        esperarErro("Data inválida! Use formato válido: 20/03/2026",
                "Titulo", "desc", "Prova", "Pendente", "2026-03-20");
    }

    @Test
    public void tituloComMaisDe150Caracteres() {
        esperarErro("O título pode ter no máximo 150 caracteres!",
                "a".repeat(151), "desc", "Prova", "Pendente", "20/03/2026");
    }

    @Test
    public void tituloComExatamente150CaracteresEhValido() throws RegraNegocioException {
        validator.validar("a".repeat(150), "desc", "Prova", "Pendente", "20/03/2026");
    }

    @Test
    public void descricaoComMaisDe1000Caracteres() {
        esperarErro("A descrição pode ter no máximo 1000 caracteres!",
                "Titulo", "a".repeat(1001), "Prova", "Pendente", "20/03/2026");
    }
}
