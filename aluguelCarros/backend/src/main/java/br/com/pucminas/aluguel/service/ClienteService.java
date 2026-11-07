package br.com.pucminas.aluguel.service;

import br.com.pucminas.aluguel.dto.ClienteAtualizacao;
import br.com.pucminas.aluguel.dto.ClienteRequest;
import br.com.pucminas.aluguel.dto.ClienteResponse;
import br.com.pucminas.aluguel.dto.RendimentoDto;
import br.com.pucminas.aluguel.model.Cliente;
import br.com.pucminas.aluguel.model.Perfil;
import br.com.pucminas.aluguel.model.Rendimento;
import br.com.pucminas.aluguel.model.Usuario;
import br.com.pucminas.aluguel.repository.ClienteRepository;
import br.com.pucminas.aluguel.repository.PedidoRepository;
import br.com.pucminas.aluguel.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ClienteResponse criar(ClienteRequest request) {
        if (usuarioRepository.existsByLogin(request.login())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login já utilizado");
        }
        if (clienteRepository.existsByCpf(request.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(request.login());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfil(Perfil.CLIENTE);
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        aplicar(cliente, request.rg(), request.cpf(), request.nome(), request.endereco(), request.profissao(),
                request.rendimentos());
        return resposta(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::resposta).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id, Usuario solicitante) {
        return resposta(obter(id, solicitante));
    }

    public ClienteResponse atualizar(Long id, ClienteAtualizacao request, Usuario solicitante) {
        Cliente cliente = obter(id, solicitante);
        if (clienteRepository.existsByCpfAndIdNot(request.cpf(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        aplicar(cliente, request.rg(), request.cpf(), request.nome(), request.endereco(), request.profissao(),
                request.rendimentos());
        return resposta(clienteRepository.save(cliente));
    }

    public void excluir(Long id, Usuario solicitante) {
        Cliente cliente = obter(id, solicitante);
        pedidoRepository.deleteByClienteId(id);
        clienteRepository.delete(cliente);
    }

    private Cliente obter(Long id, Usuario solicitante) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        boolean outroCliente = solicitante.getPerfil() == Perfil.CLIENTE
                && !cliente.getUsuario().getId().equals(solicitante.getId());
        if (outroCliente) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
        return cliente;
    }

    private void aplicar(Cliente cliente, String rg, String cpf, String nome, String endereco, String profissao,
                         List<RendimentoDto> rendimentos) {
        cliente.setRg(rg);
        cliente.setCpf(cpf);
        cliente.setNome(nome);
        cliente.setEndereco(endereco);
        cliente.setProfissao(profissao);
        cliente.getRendimentos().clear();
        if (rendimentos != null) {
            for (RendimentoDto dto : rendimentos) {
                Rendimento rendimento = new Rendimento();
                rendimento.setCliente(cliente);
                rendimento.setEntidadeEmpregadora(dto.entidadeEmpregadora());
                rendimento.setValor(dto.valor());
                cliente.getRendimentos().add(rendimento);
            }
        }
    }

    private ClienteResponse resposta(Cliente c) {
        List<RendimentoDto> rendimentos = c.getRendimentos().stream()
                .map(r -> new RendimentoDto(r.getEntidadeEmpregadora(), r.getValor())).toList();
        return new ClienteResponse(c.getId(), c.getUsuario().getLogin(), c.getRg(), c.getCpf(), c.getNome(),
                c.getEndereco(), c.getProfissao(), rendimentos);
    }
}
