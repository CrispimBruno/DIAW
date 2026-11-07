package br.com.pucminas.aluguel.service;

import br.com.pucminas.aluguel.dto.LoginRequest;
import br.com.pucminas.aluguel.dto.LoginResponse;
import br.com.pucminas.aluguel.model.Agente;
import br.com.pucminas.aluguel.model.Cliente;
import br.com.pucminas.aluguel.model.Perfil;
import br.com.pucminas.aluguel.model.Usuario;
import br.com.pucminas.aluguel.repository.AgenteRepository;
import br.com.pucminas.aluguel.repository.ClienteRepository;
import br.com.pucminas.aluguel.repository.UsuarioRepository;
import br.com.pucminas.aluguel.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository,
                       AgenteRepository agenteRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByLogin(request.login())
                .filter(u -> passwordEncoder.matches(request.senha(), u.getSenha()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));
        String nome;
        Long clienteId = null;
        if (usuario.getPerfil() == Perfil.CLIENTE) {
            Cliente cliente = clienteRepository.findByUsuarioId(usuario.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cadastro de cliente não encontrado"));
            nome = cliente.getNome();
            clienteId = cliente.getId();
        } else {
            nome = agenteRepository.findByUsuarioId(usuario.getId()).map(Agente::getNome).orElse(usuario.getLogin());
        }
        return new LoginResponse(jwtService.gerar(usuario), usuario.getPerfil(), nome, clienteId);
    }
}
