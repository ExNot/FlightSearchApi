package com.example.flightsearchapi.JwtAuth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager user(){
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password("{noop}admin")
                        .authorities("read", "write", "delete")
                        .build(),
                User.withUsername("guest")
                        .password("{noop}guest")
                        .authorities("read")
                        .build()
        );
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                /*.anyRequest().authenticated()*/
                        .requestMatchers(HttpMethod.GET).hasAuthority("read")
                        .requestMatchers(HttpMethod.POST).hasAuthority("write")
                        .requestMatchers(HttpMethod.DELETE).hasAuthority("delete")
                        .requestMatchers(HttpMethod.PUT).hasAuthority("write")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
