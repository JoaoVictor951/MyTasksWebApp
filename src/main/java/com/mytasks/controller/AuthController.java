package com.mytasks.controller;

import java.util.Map;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mytasks.dto.Dtos.CadastroRequest;
import com.mytasks.dto.Dtos.LoginRequest;
import com.mytasks.exception.NaoAutenticadoException;
import com.mytasks.exception.RegraNegocioException;
import com.mytasks.model.Usuario;
import com.mytasks.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> cadastrar(@RequestBody CadastroRequest req) throws RegraNegocioException {
        Usuario u = usuarioService.cadastrar(req.nome(), req.email(), req.senha(), req.confirmarSenha());
        return Map.of("id", u.getId(), "nome", u.getNome());
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req, HttpSession session) throws RegraNegocioException {
        Usuario u = usuarioService.autenticar(req.email(), req.senha());
        session.setAttribute(SessaoWeb.USUARIO_ID, u.getId());
        session.setAttribute(SessaoWeb.USUARIO_NOME, u.getNome());
        return Map.of("nome", u.getNome());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // O front-end usa para saber se há alguém logado e mostrar o nome.
    @GetMapping("/me")
    public Map<String, String> me(HttpSession session) {
        Object nome = session.getAttribute(SessaoWeb.USUARIO_NOME);
        if (nome == null) {
            throw new NaoAutenticadoException();
        }
        return Map.of("nome", (String) nome);
    }
}
