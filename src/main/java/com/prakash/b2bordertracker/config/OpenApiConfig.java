package com.prakash.b2bordertracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("B2B Order Tracker API")
                        .version("1.0")
                        .description(
                                "A RESTful backend simulating EDI B2B transaction flows. " +
                                        "Models the complete lifecycle of Purchase Orders (850), " +
                                        "Advance Ship Notices (856), and Invoices (810) " +
                                        "through a supply chain system."
                        )
                        .contact(new Contact()
                                .name("Prakash Sharma")
                                .url("https://github.com/PrakashSharma2000")
                        )
                );
    }
}
