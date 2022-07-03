package com.edwkaitwra.backend.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edwkaitwra.backend.config.exception.security.PostRequestAllowedOnlyException;
import com.edwkaitwra.backend.dto.LoginDTO;
import com.edwkaitwra.backend.service.UserService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edwkaitwra.backend.utils.Utils.currentDatePlusDay;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Integer accessTokenExpiredInDays;
    private final Integer refreshTokenExpiredInDays;
    private final String jwtSecret;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, Integer accessTokenExpiredInDays, Integer refreshTokenExpiredInDays, String jwtSecret, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.accessTokenExpiredInDays = accessTokenExpiredInDays;
        this.refreshTokenExpiredInDays = refreshTokenExpiredInDays;
        this.jwtSecret = jwtSecret;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream body = request.getInputStream();
            if (!"POST".equalsIgnoreCase(request.getMethod())) {
                throw new PostRequestAllowedOnlyException("Post Request Allowed Only ");
            }
            LoginDTO loginForm = objectMapper.readValue(body, LoginDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());
            log.info("Attempts Authentication with Email:" + loginForm.getEmail());
            authentication = authenticationManager.authenticate(authenticationToken);

        } catch (AuthenticationException e) {
            throwError(response, e.getMessage());
        } catch (IOException e) {
            //We need to validate that the field email or password exists in body
            throwError(response, "Email or Password Missing");
        }
        return authentication;
    }

    private void throwError(HttpServletResponse response, String errorMessage) {
        log.error("Authentication failed due to " + errorMessage);
        Map<String, String> error = new HashMap<>();
        response.setStatus(UNAUTHORIZED.value());
        error.put("status", UNAUTHORIZED.getReasonPhrase());
        error.put("timestamp", String.valueOf(LocalDateTime.now()));
        error.put("message", errorMessage);
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        userService.isActivatedByEmail(user.getUsername());

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


