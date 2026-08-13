package personal.bookerav2.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

import java.util.*;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    /**
     * Takes: a registration request (username, password, name, surname).
     * Does: checks the username is free, creates the user with a hashed
     *       password, then issues a JWT for the new user.
     * Returns: an AuthResponse carrying the token + roles.
     */
    public AuthResponse register(RegisterRequest request) {
        Optional<User> user = userRepository.findByUsername(request.username());
        if(user.isPresent()){
            throw new ResourceDuplicateException("Username already taken!");
        }
        User newUser = new User();
        Role userRole = roleRepository.findByRoleName("USER").orElseThrow(
                () -> new ResourceNotFound("Roles doesn't exists"));
        newUser.setName(request.name());
        newUser.setSurname(request.surname());
        newUser.setUsername(request.username());
        String hashPass = passwordEncoder.encode(request.password());
        newUser.setHashedPassword(hashPass);
        newUser.setRoles(new HashSet<>(Set.of(userRole)));
        userRepository.save(newUser);
        String token = jwtService.generateToken(request.username(), List.of("USER"));
        return new AuthResponse(token, request.username(), List.of("USER"));
    }

    /**
     * Takes: a login request (username, password).
     * Does: verifies the password against the stored hash; on failure throws,
     *       on success issues a JWT for the user.
     * Returns: an AuthResponse carrying the token + roles.
     */

    // TO-DO
    // add roles to User entity
    public AuthResponse login(LoginRequest request) {
        Optional<User> userToFind = userRepository.findByUsername(request.username());
        if (userToFind.isPresent()){

            if (passwordEncoder.matches(request.password(), userToFind.get().getHashedPassword())){
                String token = jwtService.generateToken(request.username(),
                        userToFind.get().getRoles().stream()
                                .map(r -> r.getRoleName())
                                .toList());
                return new AuthResponse(token, request.username(), List.of("USER"));
            }
            else{
                throw new InvalidCredentialsException("Wrong password or username");
            }
        }
        throw new InvalidCredentialsException("Wrong password or username");
    }
}