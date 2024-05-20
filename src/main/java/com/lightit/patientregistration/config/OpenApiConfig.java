package com.lightit.patientregistration.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@OpenAPIDefinition(
        info = @Info(
                title = "Light-It Patient Registration API",
                version = "1.0",
                description = "API for patient registration and management."
        )
)
public class OpenApiConfig {
}
