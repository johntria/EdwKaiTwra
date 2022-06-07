package com.edwkaitwra.backend.security;

import com.edwkaitwra.backend.filter.CustomAuthenticationFilter;
import com.edwkaitwra.backend.filter.CustomAuthorizationFilter;
import com.edwkaitwra.backend.filter.CustomCorsFilter;
import com.edwkaitwra.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Slf4j
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    @Value("${application.fe}")
    private String allowedCors;
    @Value("${key.access-token-expired}")
    private Integer accessTokenExpiredInDays;
    @Value("${key.refresh-token-expired}")
    private Integer refreshTokenExpiredInDays;
    @Value("${key.jwt-secret}")
    private String jwtSecret;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers("/open/**").permitAll();

        http
                .apply(customDsl());
        http
                .addFilterBefore(new CustomCorsFilter(allowedCors), UsernamePasswordAuthenticationFilter.class);
        http
                .addFilterBefore(new CustomAuthorizationFilter(jwtSecret, userService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    public MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {

            AuthenticationManager authenticationManager =
                    http.getSharedObject(AuthenticationManager.class);

            CustomAuthenticationFilter filter =
                    new CustomAuthenticationFilter(authenticationManager, accessTokenExpiredInDays, refreshTokenExpiredInDays, jwtSecret);
            filter.setFilterProcessesUrl("/api/**");
            http.addFilter(filter);
        }
    }

}

