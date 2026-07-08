package com.denconcept.restaurantvoting.app.config;

import com.denconcept.restaurantvoting.app.AuthUser;
import com.denconcept.restaurantvoting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating '{}'", email);
            return new AuthUser(userRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                    new UsernameNotFoundException("User not found with email = " + email))
            );
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/profile/**").hasRole("USER")
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**"))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}