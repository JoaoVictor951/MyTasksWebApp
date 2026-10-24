# MyTasksWebApp

Versão web do MyTasks (Projeto Integrador I, etapa 9). Back-end em Spring Boot (API REST) com
JDBC e MySQL, e o front-end da etapa 8 (HTML, CSS e JavaScript) ligado à API.

## Tecnologias
- Java 17 ou superior
- Spring Boot 3.5.16 (Spring Web e JDBC)
- MySQL
- JUnit 5
- NetBeans, Maven e GitHub

## Estrutura
- `model`, `dao`, `service`, `validator`, `exception`, `util`: o código das etapas 6 e 7
- `controller`: API REST (AuthController e TarefaController)
- `dto`: objetos JSON da API
- `config`: criptografia de senha (BCrypt)
- `src/main/resources/static`: páginas, CSS e JavaScript
- `src/main/resources/schema.sql`: criação das tabelas

## Como executar
1. Deixe o MySQL ligado e confira usuário e senha em `src/main/resources/application.properties`.
2. Abra a pasta no NetBeans (File > Open Project) e execute o projeto (F6).
3. Acesse http://localhost:8080 no navegador.

O banco `mytasksweb` e as tabelas são criados sozinhos na primeira execução.

## Testes
Clique com o botão direito no projeto e escolha **Test**. São 77 testes unitários, sem banco de dados.

## API
| Método | Caminho | Função |
|---|---|---|
| POST | /api/usuarios | cadastro |
| POST | /api/login | login |
| POST | /api/logout | logout |
| GET | /api/me | usuário logado |
| GET | /api/tarefas?status= | lista e filtra |
| GET | /api/tarefas/resumo | totais e percentual concluído |
| POST | /api/tarefas | cria |
| PUT | /api/tarefas/{id} | edita |
| DELETE | /api/tarefas/{id} | exclui |
