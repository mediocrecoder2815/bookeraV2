package personal.bookerav2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import personal.bookerav2.dto.auth.AuthResponse;
import personal.bookerav2.dto.auth.LoginRequest;
import personal.bookerav2.dto.auth.RegisterRequest;
import personal.bookerav2.entities.Role;
import personal.bookerav2.entities.User;
import personal.bookerav2.exceptions.InvalidCredentialsException;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.RoleRepository;
import personal.bookerav2.repository.UserRepository;
import personal.bookerav2.security.JwtService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService unit tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Role userRole;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setRoleId((short) 1);
        userRole.setRoleName("USER");

        existingUser = new User();
        existingUser.setUserId(java.util.UUID.randomUUID());
        existingUser.setUsername("johndoe");
        existingUser.setName("John");
        existingUser.setSurname("Doe");
        existingUser.setHashedPassword("hashed_password");
        existingUser.setRoles(new HashSet<>(Set.of(userRole)));
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        void shouldRegisterSuccessfully() {
            RegisterRequest request = new RegisterRequest("janedoe", "password123", "Jane", "Doe");

            when(userRepository.findByUsername("janedoe")).thenReturn(Optional.empty());
            when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(userRole));
            when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
            when(jwtService.generateToken(eq("janedoe"), any())).thenReturn("jwt_token");

            AuthResponse result = authService.register(request);

            assertNotNull(result);
            assertEquals("jwt_token", result.token());
            assertEquals("janedoe", result.username());
            assertTrue(result.roles().contains("USER"));
            verify(userRepository).save(any(User.class));
        }

        @Test
        void shouldThrowWhenUsernameTaken() {
            RegisterRequest request = new RegisterRequest("johndoe", "password123", "John", "Doe");

            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));

            assertThrows(ResourceDuplicateException.class, () -> authService.register(request));
            verify(userRepository, never()).save(any());
        }

        @Test
        void shouldThrowWhenRoleNotFound() {
            RegisterRequest request = new RegisterRequest("newuser", "password123", "New", "User");

            when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
            when(roleRepository.findByRoleName("USER")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> authService.register(request));
            verify(userRepository, never()).save(any());
        }

        @Test
        void shouldHashPasswordBeforeSaving() {
            RegisterRequest request = new RegisterRequest("secureuser", "mysecretpass", "Secure", "User");

            when(userRepository.findByUsername("secureuser")).thenReturn(Optional.empty());
            when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(userRole));
            when(passwordEncoder.encode("mysecretpass")).thenReturn("$2a$10$encodedhash");
            when(jwtService.generateToken(eq("secureuser"), any())).thenReturn("token");

            authService.register(request);

            verify(passwordEncoder).encode("mysecretpass");
            verify(userRepository).save(argThat(user ->
                user.getHashedPassword().equals("$2a$10$encodedhash")
            ));
        }

        @Test
        void shouldSetUserRolesOnNewUser() {
            RegisterRequest request = new RegisterRequest("roleuser", "pass", "Role", "User");

            when(userRepository.findByUsername("roleuser")).thenReturn(Optional.empty());
            when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(userRole));
            when(passwordEncoder.encode("pass")).thenReturn("hashed");
            when(jwtService.generateToken(eq("roleuser"), any())).thenReturn("token");

            authService.register(request);

            verify(userRepository).save(argThat(user ->
                user.getRoles().contains(userRole)
            ));
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        void shouldLoginSuccessfully() {
            LoginRequest request = new LoginRequest("johndoe", "correctpassword");

            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches("correctpassword", "hashed_password")).thenReturn(true);
            when(jwtService.generateToken(eq("johndoe"), any())).thenReturn("jwt_token");

            AuthResponse result = authService.login(request);

            assertNotNull(result);
            assertEquals("jwt_token", result.token());
            assertEquals("johndoe", result.username());
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            LoginRequest request = new LoginRequest("nonexistent", "password");

            when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

            assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        }

        @Test
        void shouldThrowWhenPasswordWrong() {
            LoginRequest request = new LoginRequest("johndoe", "wrongpassword");

            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches("wrongpassword", "hashed_password")).thenReturn(false);

            assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        }

        @Test
        void shouldReturnTokenWithUserRoles() {
            LoginRequest request = new LoginRequest("johndoe", "correctpassword");

            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches("correctpassword", "hashed_password")).thenReturn(true);
            when(jwtService.generateToken(eq("johndoe"), any())).thenReturn("jwt_token");

            AuthResponse result = authService.login(request);

            assertEquals("johndoe", result.username());
            assertNotNull(result.token());
        }
    }
}
