package br.com.pucminas.aluguel.dto;

import br.com.pucminas.aluguel.model.StatusPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequest(
        @NotNull StatusPedido status,
        @Size(max = 1000) String parecer) {
}
