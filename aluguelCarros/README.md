# Sistema de Aluguel de Carros

Laboratório 02 — DIAW, Engenharia de Software, PUC Minas.

Sistema web para gestão de aluguéis de automóveis: clientes criam, modificam, consultam e cancelam pedidos; agentes (empresas e bancos) avaliam os pedidos e executam os contratos.

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java (JDK 25) |
| Back-end | Spring Boot 4.0.x, arquitetura MVC |
| Build | Apache Maven |
| Front-end | React + Vite |
| Banco de dados | Supabase (PostgreSQL) |
| Autenticação | Login com usuário e senha (JWT) |

## Estrutura

```
backend/   API Spring Boot (controller, service, repository, model, dto, security, config)
frontend/  Aplicação React + Vite
docs/      Casos de uso, histórias do usuário, diagrama de classes e diagrama de pacotes
```

## Documentação

- [Diagrama de casos de uso](docs/casos-de-uso.md)
- [Histórias do usuário](docs/historias-de-usuario.md)
- [Diagrama de classes](docs/diagrama-de-classes.md)
- [Diagrama de pacotes](docs/diagrama-de-pacotes.md)

## Funcionalidades

- Cadastro e login obrigatórios para usar o sistema.
- CRUD de cliente (RG, CPF, nome, endereço, profissão e até 3 rendimentos com entidade empregadora).
- Cliente: criar, visualizar o status, modificar (em análise) e cancelar pedidos, com contrato de crédito opcional.
- Agente: listar pedidos, aprovar ou reprovar com parecer, executar contrato, consultar clientes.
- Automóveis com matrícula, ano, marca, modelo, placa e tipo de proprietário (cliente, empresa ou banco).

Fluxo de status: `EM_ANALISE` → `APROVADO` ou `REPROVADO`; `APROVADO` → `CONTRATADO`; cancelamento permitido antes da contratação.

## Como executar

### 1. Banco de dados (Supabase)

Crie um projeto no Supabase e copie os dados de conexão PostgreSQL (Project Settings → Database). As tabelas são criadas automaticamente na primeira execução.

### 2. Back-end

Requisitos: JDK 25 e Maven.

```bash
cd backend
export DB_URL="jdbc:postgresql://<host>:5432/postgres?sslmode=require"
export DB_USER="<usuario>"
export DB_PASSWORD="<senha>"
export JWT_SECRET="<texto com pelo menos 32 caracteres>"
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. Na primeira execução são criados dois agentes de teste (senha `123456`) e três automóveis:

| Usuário | Perfil |
|---------|--------|
| `empresa` | Agente empresa |
| `banco` | Agente banco |

### 3. Front-end

Requisitos: Node.js 20+.

```bash
cd frontend
npm install
npm run dev
```

Acesse `http://localhost:5173`. Clientes são criados pela tela de cadastro.

## Endpoints principais

| Método | Rota | Acesso |
|--------|------|--------|
| POST | `/api/auth/login` | Público |
| POST | `/api/clientes` | Público |
| GET | `/api/clientes` | Agente |
| GET, PUT, DELETE | `/api/clientes/{id}` | Próprio cliente ou agente |
| GET | `/api/automoveis`, `/api/bancos` | Autenticado |
| POST | `/api/automoveis` | Agente |
| POST | `/api/pedidos` | Cliente |
| GET | `/api/pedidos`, `/api/pedidos/{id}` | Autenticado |
| PUT, DELETE | `/api/pedidos/{id}` | Cliente dono do pedido |
| PUT | `/api/pedidos/{id}/avaliacao` | Agente |

## Autor

Crispim Bruno da Silva Junior — Engenharia de Software, PUC Minas.
