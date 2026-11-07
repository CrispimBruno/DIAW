package br.com.pucminas.aluguel.repository;

import br.com.pucminas.aluguel.model.Agente;
import br.com.pucminas.aluguel.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgenteRepository extends JpaRepository<Agente, Long> {

    Optional<Agente> findByUsuarioId(Long usuarioId);

    List<Agente> findByUsuarioPerfil(Perfil perfil);
}
