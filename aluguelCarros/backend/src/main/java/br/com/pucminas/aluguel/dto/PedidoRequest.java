package br.com.pucminas.aluguel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PedidoRequest(
        @NotNull Long automovelId,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        Long bancoId,
        @Positive BigDecimal valorCredito) {
}
