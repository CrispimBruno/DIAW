package br.com.pucminas.aluguel.controller;

import br.com.pucminas.aluguel.dto.AvaliacaoRequest;
import br.com.pucminas.aluguel.dto.PedidoRequest;
import br.com.pucminas.aluguel.dto.PedidoResponse;
import br.com.pucminas.aluguel.model.Usuario;
import br.com.pucminas.aluguel.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest request, @AuthenticationPrincipal Usuario usuario) {
        return pedidoService.criar(request, usuario);
    }

    @GetMapping
    public List<PedidoResponse> listar(@AuthenticationPrincipal Usuario usuario) {
        return pedidoService.listar(usuario);
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return pedidoService.buscar(id, usuario);
    }

    @PutMapping("/{id}")
    public PedidoResponse modificar(@PathVariable Long id, @Valid @RequestBody PedidoRequest request,
                                    @AuthenticationPrincipal Usuario usuario) {
        return pedidoService.modificar(id, request, usuario);
    }

    @DeleteMapping("/{id}")
    public PedidoResponse cancelar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return pedidoService.cancelar(id, usuario);
    }

    @PutMapping("/{id}/avaliacao")
    public PedidoResponse avaliar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequest request,
                                  @AuthenticationPrincipal Usuario usuario) {
        return pedidoService.avaliar(id, request, usuario);
    }
}
