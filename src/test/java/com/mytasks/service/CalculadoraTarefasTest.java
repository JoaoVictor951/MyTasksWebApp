package com.mytasks.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.mytasks.model.Categoria;
import com.mytasks.model.StatusTarefa;
import com.mytasks.model.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculadoraTarefasTest {

    private CalculadoraTarefas calculadora;
    private LocalDate hoje;

    @BeforeEach
    public void setUp() {
        calculadora = new CalculadoraTarefas();
        hoje = LocalDate.of(2026, 3, 20);
    }

    private Tarefa tarefa(StatusTarefa status, LocalDate entrega) {
        return new Tarefa("Tarefa", "Descricao", Categoria.TRABALHO, status, entrega, 1);
    }

    // ---- diasParaEntrega ----

    @Test
    public void diasParaEntregaNoFuturo() {
        assertEquals(5, calculadora.diasParaEntrega(hoje, LocalDate.of(2026, 3, 25)));
    }

    @Test
    public void diasParaEntregaNoMesmoDiaEhZero() {
        assertEquals(0, calculadora.diasParaEntrega(hoje, hoje));
    }

    @Test
    public void diasParaEntregaNoPassadoEhNegativo() {
        assertEquals(-3, calculadora.diasParaEntrega(hoje, LocalDate.of(2026, 3, 17)));
    }

    @Test
    public void diasParaEntregaAtravessaMes() {
        assertEquals(12, calculadora.diasParaEntrega(LocalDate.of(2026, 3, 25), LocalDate.of(2026, 4, 6)));
    }

    // ---- estaAtrasada ----

    @Test
    public void tarefaPendenteComDataPassadaEstaAtrasada() {
        assertTrue(calculadora.estaAtrasada(tarefa(StatusTarefa.PENDENTE, LocalDate.of(2026, 3, 19)), hoje));
    }

    @Test
    public void tarefaEmAndamentoComDataPassadaEstaAtrasada() {
        assertTrue(calculadora.estaAtrasada(tarefa(StatusTarefa.EM_ANDAMENTO, LocalDate.of(2026, 3, 1)), hoje));
    }

    @Test
    public void tarefaConcluidaNuncaEstaAtrasada() {
        assertFalse(calculadora.estaAtrasada(tarefa(StatusTarefa.CONCLUIDO, LocalDate.of(2026, 3, 1)), hoje));
    }

    @Test
    public void tarefaQueVenceHojeNaoEstaAtrasada() {
        assertFalse(calculadora.estaAtrasada(tarefa(StatusTarefa.PENDENTE, hoje), hoje));
    }

    @Test
    public void tarefaSemDataNaoEstaAtrasada() {
        assertFalse(calculadora.estaAtrasada(tarefa(StatusTarefa.PENDENTE, null), hoje));
    }

    // ---- prazoProximo ----

    @Test
    public void prazoProximoQuandoFaltaMenosQueOLimite() {
        assertTrue(calculadora.prazoProximo(tarefa(StatusTarefa.PENDENTE, LocalDate.of(2026, 3, 22)), hoje, 3));
    }

    @Test
    public void prazoProximoNoLimiteExato() {
        assertTrue(calculadora.prazoProximo(tarefa(StatusTarefa.PENDENTE, LocalDate.of(2026, 3, 23)), hoje, 3));
    }

    @Test
    public void prazoNaoEhProximoQuandoFaltaMaisQueOLimite() {
        assertFalse(calculadora.prazoProximo(tarefa(StatusTarefa.PENDENTE, LocalDate.of(2026, 3, 24)), hoje, 3));
    }

    @Test
    public void prazoProximoVenceHoje() {
        assertTrue(calculadora.prazoProximo(tarefa(StatusTarefa.EM_ANDAMENTO, hoje), hoje, 3));
    }

    @Test
    public void tarefaAtrasadaNaoContaComoPrazoProximo() {
        assertFalse(calculadora.prazoProximo(tarefa(StatusTarefa.PENDENTE, LocalDate.of(2026, 3, 18)), hoje, 3));
    }

    @Test
    public void tarefaConcluidaNaoContaComoPrazoProximo() {
        assertFalse(calculadora.prazoProximo(tarefa(StatusTarefa.CONCLUIDO, LocalDate.of(2026, 3, 21)), hoje, 3));
    }

    // ---- contarPorStatus e percentualConcluido ----

    private List<Tarefa> listaDeExemplo() {
        List<Tarefa> lista = new ArrayList<>();
        lista.add(tarefa(StatusTarefa.PENDENTE, hoje));
        lista.add(tarefa(StatusTarefa.EM_ANDAMENTO, hoje));
        lista.add(tarefa(StatusTarefa.CONCLUIDO, hoje));
        lista.add(tarefa(StatusTarefa.CONCLUIDO, hoje));
        return lista;
    }

    @Test
    public void contaTarefasPorStatus() {
        List<Tarefa> lista = listaDeExemplo();
        assertEquals(1, calculadora.contarPorStatus(lista, StatusTarefa.PENDENTE));
        assertEquals(1, calculadora.contarPorStatus(lista, StatusTarefa.EM_ANDAMENTO));
        assertEquals(2, calculadora.contarPorStatus(lista, StatusTarefa.CONCLUIDO));
    }

    @Test
    public void percentualConcluidoComMetadeConcluida() {
        assertEquals(50, calculadora.percentualConcluido(listaDeExemplo()));
    }

    @Test
    public void percentualConcluidoDeListaVaziaEhZero() {
        assertEquals(0, calculadora.percentualConcluido(new ArrayList<Tarefa>()));
    }

    @Test
    public void percentualConcluidoQuandoTodasConcluidas() {
        List<Tarefa> lista = new ArrayList<>();
        lista.add(tarefa(StatusTarefa.CONCLUIDO, hoje));
        lista.add(tarefa(StatusTarefa.CONCLUIDO, hoje));
        assertEquals(100, calculadora.percentualConcluido(lista));
    }

    @Test
    public void percentualConcluidoArredondaParaBaixo() {
        List<Tarefa> lista = new ArrayList<>();
        lista.add(tarefa(StatusTarefa.CONCLUIDO, hoje));
        lista.add(tarefa(StatusTarefa.PENDENTE, hoje));
        lista.add(tarefa(StatusTarefa.PENDENTE, hoje));
        // 1 de 3 = 33,33...
        assertEquals(33, calculadora.percentualConcluido(lista));
    }

    @Test
    public void percentualConcluidoArredondaParaCima() {
        List<Tarefa> lista = new ArrayList<>();
        lista.add(tarefa(StatusTarefa.CONCLUIDO, hoje));
        lista.add(tarefa(StatusTarefa.CONCLUIDO, hoje));
        lista.add(tarefa(StatusTarefa.PENDENTE, hoje));
        // 2 de 3 = 66,66...
        assertEquals(67, calculadora.percentualConcluido(lista));
    }
}
