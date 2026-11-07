package br.com.pucminas.aluguel.service;

import br.com.pucminas.aluguel.dto.AvaliacaoRequest;
import br.com.pucminas.aluguel.dto.PedidoRequest;
import br.com.pucminas.aluguel.dto.PedidoResponse;
import br.com.pucminas.aluguel.model.Agente;
import br.com.pucminas.aluguel.model.Automovel;
import br.com.pucminas.aluguel.model.Cliente;
import br.com.pucminas.aluguel.model.ContratoCredito;
import br.com.pucminas.aluguel.model.Perfil;
import br.com.pucminas.aluguel.model.PedidoAluguel;
import br.com.pucminas.aluguel.model.StatusPedido;
import br.com.pucminas.aluguel.model.Usuario;
import br.com.pucminas.aluguel.repository.AgenteRepository;
import br.com.pucminas.aluguel.repository.AutomovelRepository;
import br.com.pucminas.aluguel.repository.ClienteRepository;
import br.com.pucminas.aluguel.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final AutomovelRepository automovelRepository;
    private final AgenteRepository agenteRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                         AutomovelRepository automovelRepository, AgenteRepository agenteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.automovelRepository = automovelRepository;
        this.agenteRepository = agenteRepository;
    }

    public PedidoResponse criar(PedidoRequest request, Usuario solicitante) {
        Cliente cliente = clienteRepository.findByUsuarioId(solicitante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Cliente não encontrado"));
        PedidoAluguel pedido = new PedidoAluguel();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_ANALISE);
        pedido.setCriadoEm(LocalDateTime.now());
        preencher(pedido, request);
        return resposta(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar(Usuario solicitante) {
        List<PedidoAluguel> pedidos = solicitante.getPerfil() == Perfil.CLIENTE
                ? pedidoRepository.findByClienteIdOrderByCriadoEmDesc(clienteDe(solicitante).getId())
                : pedidoRepository.findAllByOrderByCriadoEmDesc();
        return pedidos.stream().map(this::resposta).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id, Usuario solicitante) {
        return resposta(obter(id, solicitante));
    }

    public PedidoResponse modificar(Long id, PedidoRequest request, Usuario solicitante) {
        PedidoAluguel pedido = obter(id, solicitante);
        if (pedido.getStatus() != StatusPedido.EM_ANALISE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente pedidos em análise podem ser modificados");
        }
        preencher(pedido, request);
        return resposta(pedidoRepository.save(pedido));
    }

    public PedidoResponse cancelar(Long id, Usuario solicitante) {
        PedidoAluguel pedido = obter(id, solicitante);
        if (pedido.getStatus() == StatusPedido.CONTRATADO || pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este pedido não pode ser cancelado");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        return resposta(pedidoRepository.save(pedido));
    }

    public PedidoResponse avaliar(Long id, AvaliacaoRequest request, Usuario solicitante) {
        PedidoAluguel pedido = obter(id, solicitante);
        StatusPedido atual = pedido.getStatus();
        StatusPedido novo = request.status();
        boolean valida = switch (novo) {
            case APROVADO, REPROVADO -> atual == StatusPedido.EM_ANALISE;
            case CONTRATADO -> atual == StatusPedido.APROVADO;
            default -> false;
        };
        if (!valida) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transição de status inválida");
        }
        pedido.setStatus(novo);
        pedido.setParecer(request.parecer());
        return resposta(pedidoRepository.save(pedido));
    }

    private PedidoAluguel obter(Long id, Usuario solicitante) {
        PedidoAluguel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        if (solicitante.getPerfil() == Perfil.CLIENTE
                && !pedido.getCliente().getUsuario().getId().equals(solicitante.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
        return pedido;
    }

    private Cliente clienteDe(Usuario usuario) {
        return clienteRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Cliente não encontrado"));
    }

    private void preencher(PedidoAluguel pedido, PedidoRequest request) {
        if (!request.dataFim().isAfter(request.dataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data final deve ser posterior à inicial");
        }
        Automovel automovel = automovelRepository.findById(request.automovelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Automóvel não encontrado"));
        pedido.setAutomovel(automovel);
        pedido.setDataInicio(request.dataInicio());
        pedido.setDataFim(request.dataFim());
        if (request.bancoId() == null || request.valorCredito() == null) {
            pedido.setContratoCredito(null);
            return;
        }
        Agente banco = agenteRepository.findById(request.bancoId())
                .filter(a -> a.getUsuario().getPerfil() == Perfil.AGENTE_BANCO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Banco inválido"));
        ContratoCredito contrato = pedido.getContratoCredito() == null ? new ContratoCredito() : pedido.getContratoCredito();
        contrato.setBanco(banco);
        contrato.setValor(request.valorCredito());
        pedido.setContratoCredito(contrato);
    }

    private PedidoResponse resposta(PedidoAluguel p) {
        ContratoCredito contrato = p.getContratoCredito();
        return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getCliente().getNome(),
                AutomovelService.dto(p.getAutomovel()), p.getDataInicio(), p.getDataFim(), p.getStatus(),
                p.getParecer(), contrato == null ? null : contrato.getBanco().getNome(),
                contrato == null ? null : contrato.getValor(), p.getCriadoEm());
    }
}
