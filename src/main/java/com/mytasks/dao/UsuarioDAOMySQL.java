package com.mytasks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import com.mytasks.exception.DaoException;
import com.mytasks.model.Usuario;

@Repository
public class UsuarioDAOMySQL implements UsuarioDAO {

    private final DataSource dataSource;

    public UsuarioDAOMySQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    usuario.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao cadastrar usuário", e);
        }
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nome"),
                            rs.getString("email"), rs.getString("senha"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar usuário", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir usuário", e);
        }
    }
}
