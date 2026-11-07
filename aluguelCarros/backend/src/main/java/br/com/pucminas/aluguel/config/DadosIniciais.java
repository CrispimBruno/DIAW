package br.com.pucminas.aluguel.config;

import br.com.pucminas.aluguel.model.Agente;
import br.com.pucminas.aluguel.model.Automovel;
import br.com.pucminas.aluguel.model.Perfil;
import br.com.pucminas.aluguel.model.TipoProprietario;
import br.com.pucminas.aluguel.model.Usuario;
import br.com.pucminas.aluguel.repository.AgenteRepository;
import br.com.pucminas.aluguel.repository.AutomovelRepository;
import br.com.pucminas.aluguel.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AgenteRepository agenteRepository;
    private final AutomovelRepository automovelRepository;
    private final PasswordEncoder passwordEncoder;

    public DadosIniciais(UsuarioRepository usuarioRepository, AgenteRepository agenteRepository,
                         AutomovelRepository automovelRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.agenteRepository = agenteRepository;
        this.automovelRepository = automovelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        criarAgente("empresa", "Locadora Central", Perfil.AGENTE_EMPRESA);
        criarAgente("banco", "Banco Horizonte", Perfil.AGENTE_BANCO);
        if (automovelRepository.count() == 0) {
            criarAutomovel("MAT-0001", 2023, "Toyota", "Corolla", "ABC1D23", TipoProprietario.EMPRESA);
            criarAutomovel("MAT-0002", 2024, "Volkswagen", "Polo", "BRA2E19", TipoProprietario.EMPRESA);
            criarAutomovel("MAT-0003", 2022, "Fiat", "Argo", "QWE4R56", TipoProprietario.BANCO);
        }
    }

    private void criarAgente(String login, String nome, Perfil perfil) {
        if (usuarioRepository.existsByLogin(login)) {
            return;
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(passwordEncoder.encode("123456"));
        usuario.setPerfil(perfil);
        Agente agente = new Agente();
        agente.setUsuario(usuario);
        agente.setNome(nome);
        agenteRepository.save(agente);
    }

    private void criarAutomovel(String matricula, int ano, String marca, String modelo, String placa,
                                TipoProprietario proprietario) {
        Automovel automovel = new Automovel();
        automovel.setMatricula(matricula);
        automovel.setAno(ano);
        automovel.setMarca(marca);
        automovel.setModelo(modelo);
        automovel.setPlaca(placa);
        automovel.setProprietario(proprietario);
        automovelRepository.save(automovel);
    }
}
