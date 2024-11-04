# API Gestão Vagas 📑

Bem-vindo(a) ao repositório do projeto **Gestão de Vagas**, um projeto desenvolvido para gerenciar vagas de emprego e candidaturas. Desenvolvi esse projeto para aprofundar meus conhecimentos em desenvolvimento de APIs REST com Java e Spring Boot. O projeto fornece uma API REST para auxiliar empresas e candidatos em processos de aplicação, facilitando o acompanhamento de candidatos e vagas.

## Índice
- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Endpoints](#endpoints)

---

### Sobre o Projeto

O projeto **Gestão de Vagas** visa facilitar o gerenciamento de vagas e candidaturas, e conta com as seguintes funcionalidades:

**Para Empresa**:
- Criar conta e autenticar como empresa.
- **Gestão de Vagas**: Cadastro, edição, exclusão e listagem de vagas da empresa.

**Para Candidato**
- Criar conta e autenticar como candidato.
- **Aplicação a Vagas**: Possibilita que candidatos se candidatem a vagas.
- Pesquisar por vagas.
- Ver o histórico de candidaturas feita pelo candidato.

#
### Tecnologias Utilizadas

- 🍃 **Java** com **Spring Boot**
- 🔒 **Spring Security** para autenticação e autorização 
- 📄 **Swagger** para documentação de API
- 🐳 **Docker** para containerização
- 🌶 **Lombok** para redução de código boilerplate


## Endpoints
- Endpoints de Candidato

| Método   | Endpoint                     | Descrição                                         |
|----------|------------------------------|---------------------------------------------------|
| `POST`   | `/candidate/register`        | Cria um novo candidato                            |
| `POST`   | `/candidate/auth`            | Autentica um candidato                            |
| `PUT`    | `/candidate/update`          | Atualiza um candidato                             |
| `GET`    | `/candidate/profile`         | Retorna os dados do candidato autenticado         |
| `GET`    | `/candidate/jobs?filter`     | Lista todas as vagas que correspondem a um filtro |
| `POST`   | `/candidate/application`     | Realiza uma candidatura em uma nova vaga          |
| `GET`    | `/candidate/my-applications` | Lista todas as candidaturas do candidato          |

- Endpoints de Empresa

| Método   | Endpoint                     | Descrição                                         |
|----------|------------------------------|---------------------------------------------------|
| `POST`   | `/company/register`          | Cria uma nova empresa                             |
| `POST`   | `/company/auth`              | Autentica uma empresa                             |
| `PUT`    | `/company/update`            | Atualiza uma empresa                              |
| `GET`    | `/company/profile`           | Retorna os dados da empresa autenticada           |
| `POST`   | `/company/add-job`           | Cria uma nova vaga                                |
| `PUT`    | `/company/update-job`        | Atualiza uma vaga                                 |
| `PUT`    | `/company/close-job/{id}`    | Encerra uma vaga e marca o status como `CLOSED`   |
| `GET`    | `/company/my-jobs`           | Lista todas as vagas criadas pela empresa         |

