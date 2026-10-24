// Chamadas para a API do back-end. Devolve o JSON da resposta (ou null quando não tem corpo).
async function api(url, metodo, corpo) {
    const resposta = await fetch(url, {
        method: metodo || 'GET',
        headers: { 'Content-Type': 'application/json' },
        body: corpo ? JSON.stringify(corpo) : undefined
    });

    // sem login: volta para a tela de entrada
    if (resposta.status === 401 && window.location.pathname.endsWith('tarefas.html')) {
        window.location.href = 'index.html';
        return new Promise(function () { });
    }

    let dados = null;
    try { dados = await resposta.json(); } catch (e) { }

    if (!resposta.ok) {
        throw new Error((dados && dados.mensagem) || 'Erro inesperado. Tente novamente.');
    }
    return dados;
}
