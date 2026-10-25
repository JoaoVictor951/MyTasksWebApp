package com.mytasks.validator;

import org.springframework.stereotype.Component;

import com.mytasks.exception.RegraNegocioException;
import com.mytasks.model.Categoria;
import com.mytasks.model.StatusTarefa;
import com.mytasks.util.DataUtil;

@Component
public class TarefaValidator {

    // limites iguais aos das colunas do banco de dados
    private static final int TAMANHO_MAXIMO_TITULO = 150;
    private static final int TAMANHO_MAXIMO_DESCRICAO = 1000;

    public void validar(String titulo, String descricao, String categoria,
                        String status, String dataTexto) throws RegraNegocioException {
        if (vazio(titulo) || vazio(descricao) || vazio(dataTexto)) {
            throw new RegraNegocioException("Preencha todos os campos!");
        }
        if (titulo.trim().length() > TAMANHO_MAXIMO_TITULO) {
            throw new RegraNegocioException("O título pode ter no máximo 150 caracteres!");
        }
        if (descricao.trim().length() > TAMANHO_MAXIMO_DESCRICAO) {
            throw new RegraNegocioException("A descrição pode ter no máximo 1000 caracteres!");
        }
        if (Categoria.deTexto(categoria) == null) {
            throw new RegraNegocioException("Categoria inválida!");
        }
        if (StatusTarefa.deTexto(status) == null) {
            throw new RegraNegocioException("Status inválido!");
        }
        if (DataUtil.converter(dataTexto) == null) {
            throw new RegraNegocioException("Data inválida! Use formato válido: 20/03/2026");
        }
    }

    private boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
