package br.com.pucminas.aluguel.repository;

import br.com.pucminas.aluguel.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {

    boolean existsByPlacaOrMatricula(String placa, String matricula);
}
