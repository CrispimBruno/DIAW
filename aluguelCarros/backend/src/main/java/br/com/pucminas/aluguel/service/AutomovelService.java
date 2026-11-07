package br.com.pucminas.aluguel.service;

import br.com.pucminas.aluguel.dto.AgenteDto;
import br.com.pucminas.aluguel.dto.AutomovelDto;
import br.com.pucminas.aluguel.model.Automovel;
import br.com.pucminas.aluguel.model.Perfil;
import br.com.pucminas.aluguel.repository.AgenteRepository;
import br.com.pucminas.aluguel.repository.AutomovelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AutomovelService {

    private final AutomovelRepository automovelRepository;
    private final AgenteRepository agenteRepository;

    public AutomovelService(AutomovelRepository automovelRepository, AgenteRepository agenteRepository) {
        this.automovelRepository = automovelRepository;
        this.agenteRepository = agenteRepository;
    }

    @Transactional(readOnly = true)
    public List<AutomovelDto> listar() {
        return automovelRepository.findAll().stream().map(AutomovelService::dto).toList();
    }

    public AutomovelDto criar(AutomovelDto dto) {
        if (automovelRepository.existsByPlacaOrMatricula(dto.placa(), dto.matricula())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Placa ou matrícula já cadastrada");
        }
        Automovel automovel = new Automovel();
        automovel.setMatricula(dto.matricula());
        automovel.setAno(dto.ano());
        automovel.setMarca(dto.marca());
        automovel.setModelo(dto.modelo());
        automovel.setPlaca(dto.placa());
        automovel.setProprietario(dto.proprietario());
        return dto(automovelRepository.save(automovel));
    }

    @Transactional(readOnly = true)
    public List<AgenteDto> listarBancos() {
        return agenteRepository.findByUsuarioPerfil(Perfil.AGENTE_BANCO).stream()
                .map(a -> new AgenteDto(a.getId(), a.getNome())).toList();
    }

    public static AutomovelDto dto(Automovel a) {
        return new AutomovelDto(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(), a.getModelo(), a.getPlaca(),
                a.getProprietario());
    }
}
