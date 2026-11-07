# Diagrama de Pacotes (Visão Lógica)

```mermaid
flowchart TB
    subgraph Frontend[frontend - React + Vite]
        pages[pages]
        components[components]
        api[api.js e auth.jsx]
        pages --> components
        pages --> api
    end

    subgraph Backend[backend - Spring Boot, MVC]
        controller[controller - C]
        dto[dto]
        service[service]
        model[model - M]
        repository[repository]
        security[security]
        config[config]
        controller --> dto
        controller --> service
        service --> model
        service --> repository
        service --> dto
        repository --> model
        security --> repository
        config --> security
    end

    DB[(Supabase - PostgreSQL)]

    api -->|HTTP e JSON com JWT| controller
    repository -->|JDBC| DB
```

A camada de visão (V) do MVC é o pacote `frontend`, que consome a API REST; no back-end, `controller` recebe as requisições, `service` concentra as regras de negócio e `model` e `repository` fazem o mapeamento e o acesso aos dados.
