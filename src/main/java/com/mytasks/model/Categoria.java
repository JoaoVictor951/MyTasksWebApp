package com.mytasks.model;

public enum Categoria {
    PROVA("Prova"),
    TRABALHO("Trabalho"),
    LEITURA("Leitura");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    // Converte o texto vindo da tela ou do banco. Retorna null se não existir.
    public static Categoria deTexto(String texto) {
        if (texto == null) {
            return null;
        }
        for (Categoria c : values()) {
            if (c.descricao.equalsIgnoreCase(texto.trim())) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
