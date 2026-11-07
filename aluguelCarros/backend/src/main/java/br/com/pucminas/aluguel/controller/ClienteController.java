package br.com.pucminas.aluguel.controller;

import br.com.pucminas.aluguel.dto.ClienteAtualizacao;
import br.com.pucminas.aluguel.dto.ClienteRequest;
import br.com.pucminas.aluguel.dto.ClienteResponse;
import br.com.pucminas.aluguel.model.Usuario;
import br.com.pucminas.aluguel.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest request) {
        return clienteService.criar(request);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return clienteService.buscar(id, usuario);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteAtualizacao request,
                                     @AuthenticationPrincipal Usuario usuario) {
        return clienteService.atualizar(id, request, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        clienteService.excluir(id, usuario);
    }
}
