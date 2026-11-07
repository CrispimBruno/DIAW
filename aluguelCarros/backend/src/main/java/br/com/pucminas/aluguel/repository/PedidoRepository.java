package br.com.pucminas.aluguel.repository;

import br.com.pucminas.aluguel.model.PedidoAluguel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<PedidoAluguel, Long> {

    List<PedidoAluguel> findByClienteIdOrderByCriadoEmDesc(Long clienteId);

    List<PedidoAluguel> findAllByOrderByCriadoEmDesc();

    void deleteByClienteId(Long clienteId);
}
