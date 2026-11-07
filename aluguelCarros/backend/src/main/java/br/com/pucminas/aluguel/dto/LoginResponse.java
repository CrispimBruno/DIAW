package br.com.pucminas.aluguel.dto;

import br.com.pucminas.aluguel.model.Perfil;

public record LoginResponse(String token, Perfil perfil, String nome, Long clienteId) {
}
