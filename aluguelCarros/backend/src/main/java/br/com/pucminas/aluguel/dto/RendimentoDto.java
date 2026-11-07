package br.com.pucminas.aluguel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RendimentoDto(
        @NotBlank String entidadeEmpregadora,
        @NotNull @Positive BigDecimal valor) {
}
