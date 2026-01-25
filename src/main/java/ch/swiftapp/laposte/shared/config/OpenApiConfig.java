package ch.swiftapp.laposte.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI laPosteOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("La Poste Modular API")
                .description("Swiss Postal Service Platform — Modular Monolith")
                .version("1.0.0"));
    }

    @Bean public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder().group("00-all").pathsToMatch("/api/v1/**").build();
    }
    @Bean public GroupedOpenApi userApis() {
        return GroupedOpenApi.builder().group("01-users").pathsToMatch("/api/v1/employees/**", "/api/v1/customers/**", "/api/v1/users/**").build();
    }
    @Bean public GroupedOpenApi branchApis() {
        return GroupedOpenApi.builder().group("02-branches").pathsToMatch("/api/v1/branches/**").build();
    }
    @Bean public GroupedOpenApi parcelApis() {
        return GroupedOpenApi.builder().group("03-parcels").pathsToMatch("/api/v1/parcels/**").build();
    }
    @Bean public GroupedOpenApi trackingApis() {
        return GroupedOpenApi.builder().group("04-tracking").pathsToMatch("/api/v1/tracking/**").build();
    }
    @Bean public GroupedOpenApi addressApis() {
        return GroupedOpenApi.builder().group("05-addresses").pathsToMatch("/api/v1/addresses/**").build();
    }
    @Bean public GroupedOpenApi deliveryApis() {
        return GroupedOpenApi.builder().group("06-deliveries").pathsToMatch("/api/v1/deliveries/**").build();
    }
    @Bean public GroupedOpenApi pickupApis() {
        return GroupedOpenApi.builder().group("07-pickups").pathsToMatch("/api/v1/pickups/**").build();
    }
    @Bean public GroupedOpenApi notificationApis() {
        return GroupedOpenApi.builder().group("08-notifications").pathsToMatch("/api/v1/notifications/**").build();
    }
}

