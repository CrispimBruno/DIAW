package br.com.pucminas.aluguel.security;

import br.com.pucminas.aluguel.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoMs;

    public JwtService(@Value("${app.jwt.secret}") String segredo,
                      @Value("${app.jwt.expiracao-horas}") long horas) {
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
        this.expiracaoMs = horas * 3_600_000L;
    }

    public String gerar(Usuario usuario) {
        Date agora = new Date();
        return Jwts.builder()
                .subject(String.valueOf(usuario.getId()))
                .claim("perfil", usuario.getPerfil().name())
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + expiracaoMs))
                .signWith(chave)
                .compact();
    }

    public Long extrairId(String token) {
        String assunto = Jwts.parser().verifyWith(chave).build()
                .parseSignedClaims(token).getPayload().getSubject();
        return Long.valueOf(assunto);
    }
}
