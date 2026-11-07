package br.com.pucminas.aluguel.dto;

import br.com.pucminas.aluguel.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        AutomovelDto automovel,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusPedido status,
        String parecer,
        String bancoNome,
        BigDecimal valorCredito,
        LocalDateTime criadoEm) {
}
