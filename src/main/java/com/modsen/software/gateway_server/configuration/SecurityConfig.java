package com.modsen.software.gateway_server.configuration;

import com.modsen.software.gateway_server.configuration.util.UserRole;
import com.modsen.software.gateway_server.util.AuthoritiesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static com.modsen.software.gateway_server.configuration.util.UserRole.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/api/v1/passengers").hasAnyRole(ADMIN)
                        .pathMatchers("/api/v1/passengers/**").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .pathMatchers(DELETE,"/api/v1/passengers/**").hasAnyRole(ADMIN, PASSENGER)
                        .pathMatchers(POST,"/api/v1/passengers").hasAnyRole(ADMIN)
                        .pathMatchers(PUT,"/api/v1/passengers").hasAnyRole(ADMIN, PASSENGER)
                        .pathMatchers("/api/v1/drivers").hasAnyRole(ADMIN)
                        .pathMatchers("/api/v1/drivers/**").hasAnyRole(ADMIN, DRIVER, PASSENGER)
                        .pathMatchers(DELETE, "/api/v1/drivers/**").hasAnyRole(ADMIN, DRIVER)
                        .pathMatchers(POST, "/api/v1/drivers").hasAnyRole(ADMIN, DRIVER)
                        .pathMatchers(PUT, "/api/v1/drivers").hasAnyRole(ADMIN, DRIVER)
                        .pathMatchers("/api/v1/cars").hasAnyRole(ADMIN)
                        .pathMatchers("/api/v1/cars/**").hasAnyRole(ADMIN, DRIVER, PASSENGER)
                        .pathMatchers(DELETE, "/api/v1/cars/**").hasAnyRole(ADMIN)
                        .pathMatchers(POST, "/api/v1/cars").hasAnyRole(ADMIN, DRIVER)
                        .pathMatchers(PUT, "/api/v1/cars").hasAnyRole(ADMIN, DRIVER)
                        .pathMatchers("/api/v1/ratings").hasRole(ADMIN)
                        .pathMatchers("/api/v1/ratings/passengers/**").hasAnyRole(ADMIN, PASSENGER)
                        .pathMatchers("/api/v1/ratings/drivers/**").hasAnyRole(ADMIN, DRIVER)
                        .pathMatchers("/api/v1/ratings/**").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .pathMatchers(POST, "/api/v1/ratings").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .pathMatchers(PUT, "/api/v1/ratings").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .pathMatchers(PATCH, "/api/v1/ratings/**").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .pathMatchers("/api/v1/rides").hasAnyRole(ADMIN)
                        .pathMatchers("/api/v1/rides/passengers/**").hasAnyRole(ADMIN, PASSENGER)
                        .pathMatchers("/api/v1/rides/drivers/**").hasAnyRole(ADMIN, DRIVER)
                        .pathMatchers("/api/v1/rides/drivers/**").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .pathMatchers(POST, "/api/v1/rides").hasAnyRole(ADMIN, PASSENGER)
                        .pathMatchers(PUT, "/api/v1/rides").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .pathMatchers(PATCH, "/api/v1/rides/**").hasAnyRole(ADMIN, PASSENGER, DRIVER)
                        .anyExchange().authenticated())
                .oauth2ResourceServer(
                        resourceServer -> resourceServer.jwt(
                                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(
                                        keycloakAuthConverter()
                                )
                        )
                )
                .build();
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> keycloakAuthConverter() {
        var converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                new AuthoritiesConverter()
        );
        return converter;
    }

}
