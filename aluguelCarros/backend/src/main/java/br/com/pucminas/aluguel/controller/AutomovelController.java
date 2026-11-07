package br.com.pucminas.aluguel.controller;

import br.com.pucminas.aluguel.dto.AgenteDto;
import br.com.pucminas.aluguel.dto.AutomovelDto;
import br.com.pucminas.aluguel.service.AutomovelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AutomovelController {

    private final AutomovelService automovelService;

    public AutomovelController(AutomovelService automovelService) {
        this.automovelService = automovelService;
    }

    @GetMapping("/automoveis")
    public List<AutomovelDto> listar() {
        return automovelService.listar();
    }

    @PostMapping("/automoveis")
    @ResponseStatus(HttpStatus.CREATED)
    public AutomovelDto criar(@Valid @RequestBody AutomovelDto dto) {
        return automovelService.criar(dto);
    }

    @GetMapping("/bancos")
    public List<AgenteDto> bancos() {
        return automovelService.listarBancos();
    }
}
