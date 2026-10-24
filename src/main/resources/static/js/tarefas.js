// Tela de tarefas: lista, filtro, calendário, resumo e formulário.
// Os dados vêm da API (/api/tarefas).

function $(id) { return document.getElementById(id); }

let tarefas = [];
let editandoId = null;
let mes = new Date(new Date().getFullYear(), new Date().getMonth(), 1);

const classes = { 'Pendente': 'pendente', 'Em andamento': 'andamento', 'Concluído': 'concluido' };

function paraTexto(d) {
    return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
}

function esc(texto) {
    return String(texto).replace(/[&<>"]/g, function (c) {
        return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c];
    });
}

// o servidor trabalha com dd/MM/aaaa e o campo de data com aaaa-MM-dd
function formatarData(texto) {
    const p = texto.split('-');
    return p[2] + '/' + p[1] + '/' + p[0];
}

function paraIso(texto) {
    const p = texto.split('/');
    return p[2] + '-' + p[1] + '-' + p[0];
}

async function carregar() {
    try {
        const lista = await api('/api/tarefas?status=' + encodeURIComponent($('filtroStatus').value));
        tarefas = lista.map(function (t) { t.data = paraIso(t.dataEntrega); return t; })
            .sort(function (a, b) { return a.data.localeCompare(b.data); });
        desenharLista();
        desenharCalendario();
        desenharResumo(await api('/api/tarefas/resumo'));
    } catch (erro) {
        alert(erro.message);
    }
}

function desenharResumo(r) {
    $('resumoTexto').textContent = r.total + ' tarefas | ' + r.pendentes + ' pendentes | ' +
        r.emAndamento + ' em andamento | ' + r.concluidas + ' concluídas | ' + r.atrasadas + ' atrasadas';
    $('percentual').textContent = r.percentualConcluido + '% concluído';
    $('preenchimento').style.width = r.percentualConcluido + '%';
}

function desenharLista() {
    if (tarefas.length === 0) {
        $('corpoTabela').innerHTML = '<tr><td colspan="5">Nenhuma tarefa encontrada.</td></tr>';
        return;
    }
    $('corpoTabela').innerHTML = tarefas.map(function (t) {
        const aviso = t.atrasada ? ' <span class="tag atrasada">Atrasada</span>'
            : (t.prazoProximo ? ' <span class="tag proximo">Vence em breve</span>' : '');
        return '<tr><td>' + esc(t.titulo) + '</td><td>' + t.categoria + '</td>' +
            '<td><span class="tag ' + classes[t.status] + '">' + t.status + '</span>' + aviso + '</td>' +
            '<td>' + t.dataEntrega + '</td>' +
            '<td><button class="btn" onclick="abrirModal(' + t.id + ')">Editar</button> ' +
            '<button class="btn vermelho" onclick="excluir(' + t.id + ')">Excluir</button></td></tr>';
    }).join('');
}

function desenharCalendario() {
    $('tituloMes').textContent = mes.toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' });
    const diasDoMes = new Date(mes.getFullYear(), mes.getMonth() + 1, 0).getDate();
    let html = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'].map(function (d) {
        return '<div class="semana">' + d + '</div>';
    }).join('');
    for (let i = 0; i < mes.getDay(); i++) html += '<div></div>';
    for (let dia = 1; dia <= diasDoMes; dia++) {
        const data = paraTexto(new Date(mes.getFullYear(), mes.getMonth(), dia));
        const doDia = tarefas.filter(function (t) { return t.data === data; }).map(function (t) {
            return '<span class="tag ' + classes[t.status] + '">' + esc(t.titulo) + '</span>';
        }).join('');
        html += '<div class="dia"><strong>' + dia + '</strong>' + doDia + '</div>';
    }
    $('calendario').innerHTML = html;
}

function mudarMes(n) {
    mes = new Date(mes.getFullYear(), mes.getMonth() + n, 1);
    desenharCalendario();
}

function limparErros() {
    $('erroModal').textContent = '';
    document.querySelectorAll('#modal .campo').forEach(function (c) {
        c.classList.remove('invalido');
        c.querySelector('.erro').textContent = '';
    });
}

function abrirModal(id) {
    editandoId = id || null;
    limparErros();
    const t = tarefas.find(function (x) { return x.id === id; }) ||
        { titulo: '', descricao: '', categoria: '', status: 'Pendente', data: '' };
    $('tituloModal').textContent = id ? 'Editar tarefa' : 'Nova tarefa';
    $('titulo').value = t.titulo;
    $('descricao').value = t.descricao;
    $('categoria').value = t.categoria;
    $('data').value = t.data;
    $('status').value = t.status;
    $('modal').classList.add('aberto');
}

function fecharModal() {
    $('modal').classList.remove('aberto');
}

function checar(id, mensagem) {
    const vazio = $(id).value.trim() === '';
    const campo = $(id).closest('.campo');
    campo.classList.toggle('invalido', vazio);
    campo.querySelector('.erro').textContent = vazio ? mensagem : '';
    return !vazio;
}

$('formTarefa').addEventListener('submit', async function (e) {
    e.preventDefault();
    $('erroModal').textContent = '';
    const ok = [
        checar('titulo', 'Informe o título.'),
        checar('descricao', 'Informe a descrição.'),
        checar('categoria', 'Escolha a categoria.'),
        checar('data', 'Informe uma data de entrega válida.'),
        checar('status', 'Escolha o status.')
    ].every(Boolean);
    if (!ok) return;

    const dados = {
        titulo: $('titulo').value.trim(),
        descricao: $('descricao').value.trim(),
        categoria: $('categoria').value,
        status: $('status').value,
        dataEntrega: formatarData($('data').value)
    };
    try {
        if (editandoId) {
            await api('/api/tarefas/' + editandoId, 'PUT', dados);
        } else {
            await api('/api/tarefas', 'POST', dados);
        }
        fecharModal();
        carregar();
    } catch (erro) {
        $('erroModal').textContent = erro.message;
    }
});

async function excluir(id) {
    if (!confirm('Deseja realmente excluir esta tarefa?')) return;
    try {
        await api('/api/tarefas/' + id, 'DELETE');
        carregar();
    } catch (erro) {
        alert(erro.message);
    }
}

async function sair() {
    try { await api('/api/logout', 'POST'); } catch (erro) { }
    window.location.href = 'index.html';
}

$('filtroStatus').addEventListener('change', carregar);
api('/api/me').then(function (u) { $('nomeUsuario').textContent = u.nome; });
carregar();
