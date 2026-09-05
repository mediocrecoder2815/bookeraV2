package personal.bookerav2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Service
public class JwtService {
     // base64-encoded key, must decode to >= 256 bits
    private String secret;

    private long expirationMs;

    private SecretKey signingKey;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.expirationMs = expirationMs;
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, List<String> roles) {
        if (username == null || roles == null) {
            throw new IllegalArgumentException("Wrong input");
        }
        var exp = Date.from(Instant.now().plusMillis(this.expirationMs));
        return Jwts.builder()
                .signWith(this.signingKey)
                .subject(username)
                .claim("roles", roles)
                .expiration(exp)
                .issuedAt(Date.from(Instant.now()))
                .compact();
    }

    public Claims extractAllClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Ivalid token");
        }
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        var username = this.extractAllClaims(token).getSubject();
        if (username == null || username.isBlank()) {
            return null;
        }
        return username;
    }

    public boolean isTokenValid(String token, String username) {
        if (token == null || username == null) {
            throw new IllegalArgumentException("Wrong input");
        }
        var expDate = this.extractAllClaims(token).getExpiration();
        if (!this.extractUsername(token).equals(username) ||
                !expDate.after(Date.from(Instant.now()))) {
            return false;
        }
        return true;
    }
}