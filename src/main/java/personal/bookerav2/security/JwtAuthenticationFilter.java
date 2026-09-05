package personal.bookerav2.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var reqHeader = request.getHeader("Authorization");
        if (reqHeader == null || !reqHeader.startsWith("Bearer ")){
            doFilter(request,response, filterChain);
            return;
        }
        try{
        String token = reqHeader.substring(7);
        if (jwtService.extractUsername(token) != null && SecurityContextHolder.getContext().getAuthentication() == null){
        String username = jwtService.extractUsername(token);
        if(jwtService.isTokenValid(token,username)){
            SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
            var roles = jwtService.extractAllClaims(token).get("roles", List.class);
            List<SimpleGrantedAuthority> userRoles = roles.stream()
                    .map(r -> "ROLE_" + r)
                    .map(r -> new SimpleGrantedAuthority(r.toString()))
                    .toList();
            var ctx = SecurityContextHolder.getContext();
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    userRoles
                );
            ctx.setAuthentication(auth);
            }
        }
        }
        catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validation failed", e);
            SecurityContextHolder.clearContext();
        }


        filterChain.doFilter(request, response);
    }
}