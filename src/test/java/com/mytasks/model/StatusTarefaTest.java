package com.mytasks.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class StatusTarefaTest {

    @Test
    public void convertePendente() {
        assertEquals(StatusTarefa.PENDENTE, StatusTarefa.deTexto("Pendente"));
    }

    @Test
    public void converteEmAndamento() {
        assertEquals(StatusTarefa.EM_ANDAMENTO, StatusTarefa.deTexto("Em andamento"));
    }

    @Test
    public void converteConcluidoComAcento() {
        assertEquals(StatusTarefa.CONCLUIDO, StatusTarefa.deTexto("Concluído"));
    }

    @Test
    public void converteConcluidoSemAcento() {
        assertEquals(StatusTarefa.CONCLUIDO, StatusTarefa.deTexto("Concluido"));
    }

    @Test
    public void ignoraMaiusculasEEspacos() {
        assertEquals(StatusTarefa.EM_ANDAMENTO, StatusTarefa.deTexto("  EM ANDAMENTO "));
    }

    @Test
    public void textoDesconhecidoRetornaNull() {
        assertNull(StatusTarefa.deTexto("Cancelada"));
    }

    @Test
    public void nullRetornaNull() {
        assertNull(StatusTarefa.deTexto(null));
    }

    @Test
    public void descricaoDoConcluidoTemAcento() {
        assertEquals("Concluído", StatusTarefa.CONCLUIDO.getDescricao());
    }

    @Test
    public void converteCategorias() {
        assertEquals(Categoria.PROVA, Categoria.deTexto("Prova"));
        assertEquals(Categoria.TRABALHO, Categoria.deTexto("trabalho"));
        assertEquals(Categoria.LEITURA, Categoria.deTexto(" Leitura "));
    }

    @Test
    public void categoriaDesconhecidaRetornaNull() {
        assertNull(Categoria.deTexto("Festa"));
        assertNull(Categoria.deTexto(null));
    }
}
