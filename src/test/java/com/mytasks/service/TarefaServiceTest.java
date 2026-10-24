package com.mytasks.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.mytasks.dao.memoria.TarefaDAOMemoria;
import com.mytasks.exception.RegraNegocioException;
import java.time.LocalDate;
import java.util.List;
import com.mytasks.model.Categoria;
import com.mytasks.model.StatusTarefa;
import com.mytasks.model.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mytasks.validator.TarefaValidator;

public class TarefaServiceTest {

    private static final int ANA = 1;
    private static final int BRUNO = 2;

    private TarefaService service;

    @BeforeEach
    public void setUp() throws RegraNegocioException {
        service = new TarefaService(new TarefaDAOMemoria(), new TarefaValidator());
        service.criar(ANA, "Prova de Português", "Gramática", "Prova", "Pendente", "20/03/2026");
        service.criar(ANA, "Trabalho de História", "Revolução Francesa", "Trabalho", "Em andamento", "25/03/2026");
        service.criar(ANA, "Ler capítulo 3", "Sociologia", "Leitura", "Concluído", "18/03/2026");
        service.criar(BRUNO, "Prova de Matemática", "Fórmulas", "Prova", "Pendente", "22/03/2026");
    }

    private Tarefa primeiraDaAna() throws RegraNegocioException {
        return service.listar(ANA, "Todas").get(0);
    }

    @Test
    public void criaTarefaComDadosConvertidos() throws RegraNegocioException {
        Tarefa t = service.criar(ANA, "  Estudar  ", "Revisar", "prova", "pendente", "10/04/2026");
        assertTrue(t.getId() > 0);
        assertEquals("Estudar", t.getTitulo());
        assertEquals(Categoria.PROVA, t.getCategoria());
        assertEquals(StatusTarefa.PENDENTE, t.getStatus());
        assertEquals(LocalDate.of(2026, 4, 10), t.getDataEntrega());
    }

    @Test
    public void naoCriaTarefaSemTitulo() {
        try {
            service.criar(ANA, "", "desc", "Prova", "Pendente", "20/03/2026");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Preencha todos os campos!", e.getMessage());
        }
    }

    @Test
    public void naoCriaTarefaComDataInexistente() {
        try {
            service.criar(ANA, "Teste", "desc", "Prova", "Pendente", "31/02/2026");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Data inválida! Use formato válido: 20/03/2026", e.getMessage());
        }
    }

    @Test
    public void listaTodasAsTarefasDoUsuario() throws RegraNegocioException {
        assertEquals(3, service.listar(ANA, "Todas").size());
    }

    @Test
    public void listaSemFiltroTrazTudo() throws RegraNegocioException {
        assertEquals(3, service.listar(ANA, null).size());
        assertEquals(3, service.listar(ANA, "").size());
    }

    @Test
    public void usuarioSoVeAsPropriasTarefas() throws RegraNegocioException {
        List<Tarefa> doBruno = service.listar(BRUNO, "Todas");
        assertEquals(1, doBruno.size());
        assertEquals("Prova de Matemática", doBruno.get(0).getTitulo());
    }

    @Test
    public void filtraPorCadaStatus() throws RegraNegocioException {
        assertEquals(1, service.listar(ANA, "Pendente").size());
        assertEquals(1, service.listar(ANA, "Em andamento").size());
        assertEquals(1, service.listar(ANA, "Concluído").size());
    }

    @Test
    public void filtroConcluidoFuncionaSemAcento() throws RegraNegocioException {
        assertEquals(1, service.listar(ANA, "Concluido").size());
    }

    @Test
    public void filtroComStatusInvalidoDaErro() {
        try {
            service.listar(ANA, "Sei la");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Status inválido!", e.getMessage());
        }
    }

    @Test
    public void editaTarefa() throws RegraNegocioException {
        Tarefa original = primeiraDaAna();
        service.editar(ANA, original.getId(), "Novo título", "Nova descrição", "Leitura", "Concluído", "30/03/2026");

        Tarefa editada = primeiraDaAna();
        assertEquals("Novo título", editada.getTitulo());
        assertEquals(Categoria.LEITURA, editada.getCategoria());
        assertEquals(StatusTarefa.CONCLUIDO, editada.getStatus());
        assertEquals(LocalDate.of(2026, 3, 30), editada.getDataEntrega());
        assertEquals(3, service.listar(ANA, "Todas").size());
    }

    @Test
    public void naoEditaTarefaDeOutroUsuario() throws RegraNegocioException {
        int idDaAna = primeiraDaAna().getId();
        try {
            service.editar(BRUNO, idDaAna, "Invadida", "desc", "Prova", "Pendente", "20/03/2026");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Tarefa não encontrada!", e.getMessage());
        }
        assertEquals("Prova de Português", primeiraDaAna().getTitulo());
    }

    @Test
    public void naoEditaTarefaInexistente() {
        try {
            service.editar(ANA, 9999, "Titulo", "desc", "Prova", "Pendente", "20/03/2026");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Tarefa não encontrada!", e.getMessage());
        }
    }

    @Test
    public void excluiTarefa() throws RegraNegocioException {
        service.excluir(ANA, primeiraDaAna().getId());
        assertEquals(2, service.listar(ANA, "Todas").size());
    }

    @Test
    public void naoExcluiTarefaDeOutroUsuario() throws RegraNegocioException {
        int idDaAna = primeiraDaAna().getId();
        try {
            service.excluir(BRUNO, idDaAna);
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Tarefa não encontrada!", e.getMessage());
        }
        assertEquals(3, service.listar(ANA, "Todas").size());
    }

    @Test
    public void naoExcluiTarefaDuasVezes() throws RegraNegocioException {
        int id = primeiraDaAna().getId();
        service.excluir(ANA, id);
        try {
            service.excluir(ANA, id);
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Tarefa não encontrada!", e.getMessage());
        }
    }
}
