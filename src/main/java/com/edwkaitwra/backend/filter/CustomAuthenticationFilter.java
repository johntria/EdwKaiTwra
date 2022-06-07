package com.edwkaitwra.backend.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edwkaitwra.backend.utils.Utills.currentDatePlusDay;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final Integer accessTokenExpiredInDays;
    private final Integer refreshTokenExpiredInDays;
    private final String jwtSecret;


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, Integer accessTokenExpiredInDays, Integer refreshTokenExpiredInDays, String jwtSecret) {
        this.authenticationManager = authenticationManager;
        this.accessTokenExpiredInDays = accessTokenExpiredInDays;
        this.refreshTokenExpiredInDays = refreshTokenExpiredInDays;
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        this.logger.warn("Initialized Authentication Filter");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Authentication authentication = null;
        log.info("Attempts Authentication with Email:" + email);

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            this.logger.error("Authentication fail due to " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            response.setStatus(UNAUTHORIZED.value());
            error.put("status", UNAUTHORIZED.getReasonPhrase());
            error.put("timestamp", String.valueOf(LocalDateTime.now()));
            error.put("message", e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

        return authentication;

    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        String accessToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(currentDatePlusDay(accessTokenExpiredInDays)).withIssuer(request.getRequestURL().toString()).withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);

        String refreshToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(currentDatePlusDay(refreshTokenExpiredInDays)).withIssuer(request.getRequestURL().toString()).withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }


}


