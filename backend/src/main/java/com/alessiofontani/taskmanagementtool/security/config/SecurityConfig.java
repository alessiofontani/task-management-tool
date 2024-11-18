package com.alessiofontani.taskmanagementtool.security.config;

import com.alessiofontani.taskmanagementtool.security.AuthExceptionHandler;
import com.alessiofontani.taskmanagementtool.security.jwt.JwtValidationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AuthExceptionHandler authExceptionHandler;

    @Autowired
    JwtValidationFilter jwtValidationFilter;

    @Value("cors.allowed-origins")
    private String allowedOrigins;

    @Value("cors.allowed-methods")
    private String allowedMethods;

    @Value("cors.allowed-headers")
    private String allowedHeaders;

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors-> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(handle -> handle.authenticationEntryPoint(authExceptionHandler))
            .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth-> auth
                    .requestMatchers("/error**", "/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .build();
    }



}
