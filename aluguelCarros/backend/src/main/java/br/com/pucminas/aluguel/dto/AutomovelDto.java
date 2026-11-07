package br.com.pucminas.aluguel.dto;

import br.com.pucminas.aluguel.model.TipoProprietario;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AutomovelDto(
        Long id,
        @NotBlank String matricula,
        @NotNull @Min(1950) Integer ano,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotBlank String placa,
        @NotNull TipoProprietario proprietario) {
}
