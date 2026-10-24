package com.mytasks.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import com.mytasks.exception.DaoException;
import com.mytasks.model.Categoria;
import com.mytasks.model.StatusTarefa;
import com.mytasks.model.Tarefa;

@Repository
public class TarefaDAOMySQL implements TarefaDAO {

    private final DataSource dataSource;

    public TarefaDAOMySQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void inserir(Tarefa tarefa) {
        String sql = "INSERT INTO tarefa (titulo, descricao, categoria, status, data_entrega, usuario_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getCategoria().getDescricao());
            stmt.setString(4, tarefa.getStatus().getDescricao());
            stmt.setDate(5, Date.valueOf(tarefa.getDataEntrega()));
            stmt.setInt(6, tarefa.getUsuarioId());
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    tarefa.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao salvar tarefa", e);
        }
    }

    @Override
    public void atualizar(Tarefa tarefa) {
        String sql = "UPDATE tarefa SET titulo=?, descricao=?, categoria=?, status=?, data_entrega=? WHERE id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getCategoria().getDescricao());
            stmt.setString(4, tarefa.getStatus().getDescricao());
            stmt.setDate(5, Date.valueOf(tarefa.getDataEntrega()));
            stmt.setInt(6, tarefa.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar tarefa", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM tarefa WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir tarefa", e);
        }
    }

    @Override
    public Tarefa buscarPorId(int id) {
        String sql = "SELECT * FROM tarefa WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? lerTarefa(rs) : null;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar tarefa", e);
        }
    }

    @Override
    public List<Tarefa> listarPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM tarefa WHERE usuario_id = ?";
        List<Tarefa> tarefas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tarefas.add(lerTarefa(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar tarefas", e);
        }
        return tarefas;
    }

    private Tarefa lerTarefa(ResultSet rs) throws SQLException {
        return new Tarefa(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                Categoria.deTexto(rs.getString("categoria")),
                StatusTarefa.deTexto(rs.getString("status")),
                rs.getDate("data_entrega").toLocalDate(),
                rs.getInt("usuario_id"));
    }
}
