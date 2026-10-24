package com.mytasks.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.mytasks.dao.memoria.UsuarioDAOMemoria;
import com.mytasks.exception.RegraNegocioException;
import com.mytasks.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.mytasks.validator.UsuarioValidator;


public class UsuarioServiceTest {

    private UsuarioService service;
    private PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        encoder = new BCryptPasswordEncoder();
        service = new UsuarioService(new UsuarioDAOMemoria(), new UsuarioValidator(), encoder);
    }

    @Test
    public void cadastraUsuarioValido() throws RegraNegocioException {
        Usuario u = service.cadastrar("Ana", "ana@teste.com", "123", "123");
        assertNotNull(u);
        assertTrue(u.getId() > 0);
        assertEquals("ana@teste.com", u.getEmail());
    }

    @Test
    public void senhaNaoEhGravadaEmTextoPuro() throws RegraNegocioException {
        Usuario u = service.cadastrar("Ana", "ana@teste.com", "123456", "123456");
        assertNotEquals("123456", u.getSenha());
        assertTrue(encoder.matches("123456", u.getSenha()));
    }

    @Test
    public void naoCadastraComCampoVazio() {
        try {
            service.cadastrar("", "ana@teste.com", "123", "123");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Preencha todos os campos!", e.getMessage());
        }
    }

    @Test
    public void naoCadastraComSenhasDiferentes() {
        try {
            service.cadastrar("Ana", "ana@teste.com", "123", "999");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("As senhas precisam ser iguais!", e.getMessage());
        }
    }

    @Test
    public void naoCadastraEmailRepetido() throws RegraNegocioException {
        service.cadastrar("Ana", "ana@teste.com", "123", "123");
        try {
            service.cadastrar("Outra Ana", "ana@teste.com", "456", "456");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("E-mail já cadastrado!", e.getMessage());
        }
    }

    @Test
    public void loginComDadosCorretos() throws RegraNegocioException {
        Usuario criado = service.cadastrar("Ana", "ana@teste.com", "123", "123");
        Usuario logado = service.autenticar("ana@teste.com", "123");
        assertEquals(criado.getId(), logado.getId());
    }

    @Test
    public void loginComSenhaErrada() throws RegraNegocioException {
        service.cadastrar("Ana", "ana@teste.com", "123", "123");
        try {
            service.autenticar("ana@teste.com", "errada");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Email ou senha inválidos!", e.getMessage());
        }
    }

    @Test
    public void loginComEmailInexistente() {
        try {
            service.autenticar("naoexiste@teste.com", "123");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Email ou senha inválidos!", e.getMessage());
        }
    }

    @Test
    public void loginComCampoVazio() {
        try {
            service.autenticar("ana@teste.com", "");
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Preencha todos os campos!", e.getMessage());
        }
    }
}
