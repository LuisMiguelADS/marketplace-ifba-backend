package com.marketplace.ifba.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class    SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,  "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/register").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/area").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/area/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/area/**").hasRole("ADMIN")
                        /*.requestMatchers(HttpMethod.POST,  "/propostas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/propostas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/projetos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/projetos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/organizacoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/organizacoes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/ofertas-solucao").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/ofertas-solucao/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/instituicoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/instituicoes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/grupos-pesquisa").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/grupos-pesquisa/**").hasRole("ADMIN")*/
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
