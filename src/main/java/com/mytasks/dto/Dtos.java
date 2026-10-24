package com.mytasks.dto;

// Objetos que viajam em JSON entre o front-end e a API.
public final class Dtos {

    private Dtos() {
    }

    public record CadastroRequest(String nome, String email, String senha, String confirmarSenha) {
    }

    public record LoginRequest(String email, String senha) {
    }

    // dataEntrega no formato dd/MM/aaaa
    public record TarefaRequest(String titulo, String descricao, String categoria, String status, String dataEntrega) {
    }

    public record TarefaResponse(int id, String titulo, String descricao, String categoria, String status,
                                 String dataEntrega, boolean atrasada, boolean prazoProximo) {
    }

    public record ResumoResponse(int total, int pendentes, int emAndamento, int concluidas,
                                 int atrasadas, int percentualConcluido) {
    }
}
