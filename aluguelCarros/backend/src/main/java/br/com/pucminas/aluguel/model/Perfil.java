package br.com.pucminas.aluguel.model;

public enum Perfil {
    CLIENTE,
    AGENTE_EMPRESA,
    AGENTE_BANCO;

    public String papel() {
        return this == CLIENTE ? "ROLE_CLIENTE" : "ROLE_AGENTE";
    }
}
