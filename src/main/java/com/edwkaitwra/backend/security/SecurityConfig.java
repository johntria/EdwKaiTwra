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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig {

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
        //We have to disable it due to we have custom authentication mechanism .
        http.csrf().disable();

        //Authentication Filter
        http.apply(customDsl());

        //Cors Filter
        CustomCorsFilter customCorsFilter = new CustomCorsFilter(allowedCors);
        http.addFilterBefore(customCorsFilter, CustomAuthenticationFilter.class);

        //Authorization Filter
        CustomAuthorizationFilter customAuthorizationFilter = new CustomAuthorizationFilter(jwtSecret, userService);
        http.addFilterBefore(customAuthorizationFilter, CustomAuthenticationFilter.class);


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
                    new CustomAuthenticationFilter(authenticationManager, accessTokenExpiredInDays, refreshTokenExpiredInDays, jwtSecret, userService);
            filter.setFilterProcessesUrl("/api/open/login");
            http.addFilter(filter);
        }
    }

}

