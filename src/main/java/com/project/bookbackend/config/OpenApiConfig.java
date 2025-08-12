package com.project.bookbackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(
	contact = @Contact(
		name = "Parth Srivastav",
		email = "parthsrivastav.00@gmail.com"
	),
	description = "OpenApi documentation",
	title = "OpenApi specification - Parth",
	version = "1.0",
	license = @License(
		name = "MIT License",
		url = "https://dontknowwhaturl.com"
	),
	termsOfService = "None of ya business"
),
	servers = {
		@Server(
			description = "Local ENV",
			url = "http://localhost:8088/api/v1"
		),
		@Server(
			description = "Prod ENV",
			url = "https://srivastavcodes.com/codesamples"
		)
	},
	security = {
		@SecurityRequirement(
			name = "bearerAuth"
		)
	}
)
@SecurityScheme(
	name = "bearerAuth",
	description = "JWT Auth description",
	scheme = "bearer",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
