package br.com.pucminas.aluguel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClienteRequest(
        @NotBlank @Size(min = 3, max = 40) String login,
        @NotBlank @Size(min = 6, max = 72) String senha,
        @NotBlank String rg,
        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos") String cpf,
        @NotBlank String nome,
        @NotBlank String endereco,
        @NotBlank String profissao,
        @Valid @Size(max = 3, message = "No máximo 3 rendimentos") List<RendimentoDto> rendimentos) {
}
