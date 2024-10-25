package com.modsen.software.gateway_server.configuration;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("rides_service", r -> r.path("/api/v1/rides/**")
                        .uri("lb://rides-service/api/v1/rides"))
                .route("passengers_service", r -> r.path("/api/v1/passengers/**")
                        .uri("lb://passenger-service/api/v1/passengers"))
                .route("drivers_service", r -> r.path("/api/v1/drivers/**")
                        .uri("lb://driver-service/api/v1/drivers"))
                .route("cars_service", r -> r.path("/api/v1/cars/**")
                        .uri("lb://driver-service/api/v1/cars"))
                .route("ratings_service", r -> r.path("/api/v1/ratings/**")
                        .uri("lb://ratings-service/api/v1/ratings"))
                .build();
    }
}
