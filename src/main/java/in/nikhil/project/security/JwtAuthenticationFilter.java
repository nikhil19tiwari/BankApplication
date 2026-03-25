package in.nikhil.project.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.nikhil.project.payload.LoginRequest;
import in.nikhil.project.payload.LoginResponse;
import in.nikhil.project.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {

        if (!request.getServletPath().equals("/api/v1/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    );
            Authentication authResult = authenticationManager.authenticate(authToken);
            if (authResult.isAuthenticated()) {
                String token = jwtUtil.generateToken(loginRequest.getEmail());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                LoginResponse loginResponse = new LoginResponse(token);
                mapper.writeValue(response.getWriter(), loginResponse);
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                mapper.writeValue(response.getWriter(), Map.of("error", "Authentication failed"));
                return;
            }
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), Map.of("error", "Invalid credentials"));
            return;
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), Map.of("error", "Bad request", "details", ex.getMessage()));
            return;
        }
    }
}