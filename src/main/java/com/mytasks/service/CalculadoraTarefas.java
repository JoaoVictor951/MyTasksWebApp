package com.mytasks.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import com.mytasks.model.StatusTarefa;
import com.mytasks.model.Tarefa;

// Regras de cálculo sobre as tarefas (prazos e andamento).
// Não acessa banco nem tela, recebe tudo por parâmetro. Por isso é fácil de testar
// e vai servir para os lembretes de prazo e para o resumo da versão web.
@Component
public class CalculadoraTarefas {

    // Dias entre hoje e a entrega. Se a entrega já passou, o resultado é negativo.
    public long diasParaEntrega(LocalDate hoje, LocalDate entrega) {
        return ChronoUnit.DAYS.between(hoje, entrega);
    }

    // Está atrasada se a data de entrega já passou e a tarefa não foi concluída.
    public boolean estaAtrasada(Tarefa tarefa, LocalDate hoje) {
        if (tarefa.getStatus() == StatusTarefa.CONCLUIDO || tarefa.getDataEntrega() == null) {
            return false;
        }
        return tarefa.getDataEntrega().isBefore(hoje);
    }

    // Prazo próximo: ainda não venceu, não está concluída e falta no máximo "dias" dias.
    public boolean prazoProximo(Tarefa tarefa, LocalDate hoje, int dias) {
        if (tarefa.getStatus() == StatusTarefa.CONCLUIDO || tarefa.getDataEntrega() == null) {
            return false;
        }
        if (estaAtrasada(tarefa, hoje)) {
            return false;
        }
        return diasParaEntrega(hoje, tarefa.getDataEntrega()) <= dias;
    }

    public int contarPorStatus(List<Tarefa> tarefas, StatusTarefa status) {
        int total = 0;
        for (Tarefa t : tarefas) {
            if (t.getStatus() == status) {
                total++;
            }
        }
        return total;
    }

    // Percentual de tarefas concluídas, arredondado. Lista vazia dá 0.
    public int percentualConcluido(List<Tarefa> tarefas) {
        if (tarefas.isEmpty()) {
            return 0;
        }
        int concluidas = contarPorStatus(tarefas, StatusTarefa.CONCLUIDO);
        return (int) Math.round(concluidas * 100.0 / tarefas.size());
    }
}
