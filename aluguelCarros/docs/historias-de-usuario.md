# Histórias do Usuário

| ID | Como | Quero | Para | Critérios de aceitação |
|----|------|-------|------|------------------------|
| HU01 | Cliente | me cadastrar informando RG, CPF, nome, endereço, profissão e até 3 rendimentos | poder usar o sistema | CPF único; no máximo 3 rendimentos; senha com no mínimo 6 caracteres |
| HU02 | Usuário | entrar com usuário e senha | acessar as funcionalidades do meu perfil | Acesso negado sem login; token expira em 8 horas |
| HU03 | Cliente | consultar, alterar e excluir meus dados | manter meu cadastro atualizado | Só acessa o próprio cadastro; excluir remove também os pedidos |
| HU04 | Cliente | criar um pedido de aluguel escolhendo automóvel e período | solicitar um carro | Data final posterior à inicial; pedido nasce "Em análise" |
| HU05 | Cliente | associar um contrato de crédito de um banco ao pedido | financiar o aluguel | Banco e valor opcionais; banco deve ser agente do tipo banco |
| HU06 | Cliente | visualizar o status dos meus pedidos | acompanhar a análise | Lista em ordem do mais recente; exibe parecer do agente |
| HU07 | Cliente | modificar um pedido em análise | corrigir período ou automóvel | Permitido somente em "Em análise" |
| HU08 | Cliente | cancelar um pedido | desistir do aluguel | Não cancela pedido já contratado |
| HU09 | Agente | listar todos os pedidos | analisá-los | Visível apenas para agentes |
| HU10 | Agente | aprovar ou reprovar um pedido com parecer | concluir a análise financeira | Transição permitida somente a partir de "Em análise" |
| HU11 | Agente | executar o contrato de um pedido aprovado | formalizar o aluguel | Transição permitida somente a partir de "Aprovado" |
| HU12 | Agente | consultar clientes cadastrados | apoiar a análise | Lista com identificação, profissão e rendimentos |
| HU13 | Agente | cadastrar automóveis | disponibilizá-los para aluguel | Matrícula e placa únicas; proprietário cliente, empresa ou banco |
