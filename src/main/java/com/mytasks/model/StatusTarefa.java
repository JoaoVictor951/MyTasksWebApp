package com.mytasks.model;

import java.text.Normalizer;

public enum StatusTarefa {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído");

    private final String descricao;

    StatusTarefa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    // Aceita com ou sem acento, porque no sistema desktop os dois jeitos
    // foram gravados no banco ("Concluido" e "Concluído").
    public static StatusTarefa deTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String entrada = semAcento(texto.trim());
        for (StatusTarefa s : values()) {
            if (semAcento(s.descricao).equalsIgnoreCase(entrada)) {
                return s;
            }
        }
        return null;
    }

    private static String semAcento(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    @Override
    public String toString() {
        return descricao;
    }
}
