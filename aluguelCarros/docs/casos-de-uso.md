# Diagrama de Casos de Uso

```mermaid
flowchart LR
    Cliente([Cliente])
    Agente([Agente: empresa ou banco])

    subgraph Sistema[Sistema de Aluguel de Carros]
        UC1((Cadastrar-se))
        UC2((Autenticar-se))
        UC3((Gerenciar dados do cliente))
        UC4((Introduzir pedido de aluguel))
        UC5((Consultar pedidos))
        UC6((Modificar pedido))
        UC7((Cancelar pedido))
        UC8((Avaliar pedido financeiramente))
        UC9((Executar contrato))
        UC10((Consultar clientes))
        UC11((Cadastrar automóvel))
        UC12((Associar contrato de crédito))
    end

    Cliente --- UC1
    Cliente --- UC2
    Cliente --- UC3
    Cliente --- UC4
    Cliente --- UC5
    Cliente --- UC6
    Cliente --- UC7

    Agente --- UC2
    Agente --- UC5
    Agente --- UC8
    Agente --- UC9
    Agente --- UC10
    Agente --- UC11

    UC4 -.->|include| UC2
    UC4 -.->|extend| UC12
    UC9 -.->|include| UC8
```
