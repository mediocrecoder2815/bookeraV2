package personal.bookerav2.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtService unit tests")
class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "dGhpcyBpcyBhIHZlcnkgbG9uZyBzZWNyZXQga2V5IGZvciBqd3QgdG9rZW5zIGdlbmVyYXRpb24=";
    private static final long EXPIRATION_MS = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, EXPIRATION_MS);
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {

        @Test
        void shouldGenerateToken() {
            String token = jwtService.generateToken("johndoe", List.of("USER"));

            assertNotNull(token);
            assertFalse(token.isBlank());
        }

        @Test
        void shouldContainCorrectUsername() {
            String token = jwtService.generateToken("johndoe", List.of("USER"));

            String username = jwtService.extractUsername(token);
            assertEquals("johndoe", username);
        }

        @Test
        void shouldContainRoles() {
            String token = jwtService.generateToken("johndoe", List.of("USER", "ADMIN"));

            Claims claims = jwtService.extractAllClaims(token);
            List<String> roles = claims.get("roles", List.class);
            assertEquals(List.of("USER", "ADMIN"), roles);
        }

        @Test
        void shouldThrowWhenUsernameNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> jwtService.generateToken(null, List.of("USER")));
        }

        @Test
        void shouldThrowWhenRolesNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> jwtService.generateToken("johndoe", null));
        }
    }

    @Nested
    @DisplayName("extractAllClaims")
    class ExtractAllClaims {

        @Test
        void shouldExtractClaimsFromValidToken() {
            String token = jwtService.generateToken("johndoe", List.of("USER"));

            Claims claims = jwtService.extractAllClaims(token);

            assertNotNull(claims);
            assertEquals("johndoe", claims.getSubject());
        }

        @Test
        void shouldThrowWhenTokenNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> jwtService.extractAllClaims(null));
        }

        @Test
        void shouldThrowWhenTokenBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> jwtService.extractAllClaims("   "));
        }

        @Test
        void shouldThrowWhenTokenInvalid() {
            assertThrows(Exception.class,
                    () -> jwtService.extractAllClaims("invalid.token.here"));
        }
    }

    @Nested
    @DisplayName("extractUsername")
    class ExtractUsername {

        @Test
        void shouldExtractUsername() {
            String token = jwtService.generateToken("janedoe", List.of("USER"));

            String username = jwtService.extractUsername(token);

            assertEquals("janedoe", username);
        }

        @Test
        void shouldReturnNullForInvalidToken() {
            assertThrows(Exception.class,
                    () -> jwtService.extractUsername("garbage"));
        }
    }

    @Nested
    @DisplayName("isTokenValid")
    class IsTokenValid {

        @Test
        void shouldReturnTrueForValidToken() {
            String token = jwtService.generateToken("johndoe", List.of("USER"));

            boolean valid = jwtService.isTokenValid(token, "johndoe");

            assertTrue(valid);
        }

        @Test
        void shouldReturnFalseForWrongUsername() {
            String token = jwtService.generateToken("johndoe", List.of("USER"));

            boolean valid = jwtService.isTokenValid(token, "wronguser");

            assertFalse(valid);
        }

        @Test
        void shouldReturnFalseForExpiredToken() {
            JwtService shortLivedService = new JwtService(SECRET, -1);
            String token = shortLivedService.generateToken("johndoe", List.of("USER"));

            assertThrows(Exception.class,
                    () -> jwtService.isTokenValid(token, "johndoe"));
        }

        @Test
        void shouldThrowWhenTokenNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> jwtService.isTokenValid(null, "johndoe"));
        }

        @Test
        void shouldThrowWhenUsernameNull() {
            String token = jwtService.generateToken("johndoe", List.of("USER"));
            assertThrows(IllegalArgumentException.class,
                    () -> jwtService.isTokenValid(token, null));
        }
    }
}
