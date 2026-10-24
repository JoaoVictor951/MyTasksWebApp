// Validações das telas de login e cadastro
const ehCadastro = document.getElementById('formCadastro') !== null;

function valor(id) {
    return document.getElementById(id).value.trim();
}

function mostrarErro(id, mensagem) {
    const campo = document.getElementById(id).closest('.campo');
    campo.classList.toggle('invalido', mensagem !== '');
    campo.querySelector('.erro').textContent = mensagem;
    return mensagem === '';
}

function validarNome() {
    return mostrarErro('nome', valor('nome') === '' ? 'Informe o nome.' : '');
}

function validarEmail() {
    const email = valor('email');
    if (email === '') return mostrarErro('email', 'Informe o e-mail.');
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return mostrarErro('email', 'E-mail inválido.');
    return mostrarErro('email', '');
}

function validarSenha() {
    const senha = document.getElementById('senha').value;
    if (senha === '') return mostrarErro('senha', 'Informe a senha.');
    if (ehCadastro && senha.length < 6) return mostrarErro('senha', 'A senha precisa ter pelo menos 6 caracteres.');
    return mostrarErro('senha', '');
}

function validarConfirmacao() {
    const confirmar = document.getElementById('confirmar').value;
    if (confirmar === '') return mostrarErro('confirmar', 'Confirme a senha.');
    if (confirmar !== document.getElementById('senha').value) return mostrarErro('confirmar', 'As senhas precisam ser iguais.');
    return mostrarErro('confirmar', '');
}

function mostrarMensagem(texto) {
    const aviso = document.getElementById('mensagemErro');
    aviso.textContent = texto;
    aviso.style.display = texto === '' ? 'none' : 'block';
}

const formLogin = document.getElementById('formLogin');
if (formLogin) {
    formLogin.addEventListener('submit', async function (e) {
        e.preventDefault();
        mostrarMensagem('');
        if (![validarEmail(), validarSenha()].every(Boolean)) return;
        try {
            await api('/api/login', 'POST', { email: valor('email'), senha: document.getElementById('senha').value });
            window.location.href = 'tarefas.html';
        } catch (erro) {
            mostrarMensagem(erro.message);
        }
    });
}

const formCadastro = document.getElementById('formCadastro');
if (formCadastro) {
    formCadastro.addEventListener('submit', async function (e) {
        e.preventDefault();
        mostrarMensagem('');
        if (![validarNome(), validarEmail(), validarSenha(), validarConfirmacao()].every(Boolean)) return;
        try {
            await api('/api/usuarios', 'POST', {
                nome: valor('nome'),
                email: valor('email'),
                senha: document.getElementById('senha').value,
                confirmarSenha: document.getElementById('confirmar').value
            });
            document.getElementById('mensagem').style.display = 'block';
            setTimeout(function () { window.location.href = 'index.html'; }, 1500);
        } catch (erro) {
            mostrarMensagem(erro.message);
        }
    });
    document.getElementById('nome').addEventListener('blur', validarNome);
    document.getElementById('confirmar').addEventListener('blur', validarConfirmacao);
}

if (document.getElementById('email')) {
    document.getElementById('email').addEventListener('blur', validarEmail);
    document.getElementById('senha').addEventListener('blur', validarSenha);
}
