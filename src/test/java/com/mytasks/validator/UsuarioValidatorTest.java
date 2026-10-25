package com.mytasks.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mytasks.exception.RegraNegocioException;

public class UsuarioValidatorTest {

    private UsuarioValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UsuarioValidator();
    }

    private void esperarErroNoCadastro(String mensagem, String nome, String email, String senha, String confirmar) {
        try {
            validator.validarCadastro(nome, email, senha, confirmar);
            fail("Era esperado o erro: " + mensagem);
        } catch (RegraNegocioException e) {
            assertEquals(mensagem, e.getMessage());
        }
    }

    @Test
    public void cadastroValidoNaoLancaErro() throws RegraNegocioException {
        validator.validarCadastro("Ana", "ana@teste.com", "123456", "123456");
    }

    @Test
    public void nomeComMaisDe100Caracteres() {
        esperarErroNoCadastro("O nome pode ter no máximo 100 caracteres!",
                "a".repeat(101), "ana@teste.com", "123456", "123456");
    }

    @Test
    public void emailComMaisDe150Caracteres() {
        esperarErroNoCadastro("O e-mail pode ter no máximo 150 caracteres!",
                "Ana", "a".repeat(145) + "@t.com", "123456", "123456");
    }

    @Test
    public void senhaComMaisDe72Caracteres() {
        esperarErroNoCadastro("A senha pode ter no máximo 72 caracteres!",
                "Ana", "ana@teste.com", "a".repeat(80), "a".repeat(80));
    }

    @Test
    public void senhaCom72CaracteresEhValida() throws RegraNegocioException {
        validator.validarCadastro("Ana", "ana@teste.com", "a".repeat(72), "a".repeat(72));
    }

    @Test
    public void loginComSenhaGrandeDemaisEhRecusado() {
        try {
            validator.validarLogin("ana@teste.com", "a".repeat(80));
            fail("Deveria dar erro");
        } catch (RegraNegocioException e) {
            assertEquals("Email ou senha inválidos!", e.getMessage());
        }
    }
}
