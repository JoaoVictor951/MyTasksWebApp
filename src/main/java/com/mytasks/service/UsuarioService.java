package com.mytasks.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mytasks.dao.UsuarioDAO;
import com.mytasks.exception.RegraNegocioException;
import com.mytasks.model.Usuario;
import com.mytasks.validator.UsuarioValidator;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final UsuarioValidator validator;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioDAO usuarioDAO, UsuarioValidator validator, PasswordEncoder encoder) {
        this.usuarioDAO = usuarioDAO;
        this.validator = validator;
        this.encoder = encoder;
    }

    public Usuario cadastrar(String nome, String email, String senha, String confirmarSenha)
            throws RegraNegocioException {
        validator.validarCadastro(nome, email, senha, confirmarSenha);

        if (usuarioDAO.buscarPorEmail(email.trim()) != null) {
            throw new RegraNegocioException("E-mail já cadastrado!");
        }

        // a senha nunca vai para o banco em texto puro
        Usuario usuario = new Usuario(nome.trim(), email.trim(), encoder.encode(senha));
        usuarioDAO.inserir(usuario);
        return usuario;
    }

    public Usuario autenticar(String email, String senha) throws RegraNegocioException {
        validator.validarLogin(email, senha);

        Usuario usuario = usuarioDAO.buscarPorEmail(email.trim());
        if (usuario == null || !encoder.matches(senha, usuario.getSenha())) {
            throw new RegraNegocioException("Email ou senha inválidos!");
        }
        return usuario;
    }
}
