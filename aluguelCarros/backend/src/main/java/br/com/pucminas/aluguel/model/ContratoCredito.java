package br.com.pucminas.aluguel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "contratos_credito")
@Getter
@Setter
@NoArgsConstructor
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "banco_id", nullable = false)
    private Agente banco;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;
}
