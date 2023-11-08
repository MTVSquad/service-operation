package com.vsquad.iroas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(
        info = @Info(
                title = "iroas",
                description = "iroas 백엔드 API 명세서 \n" +
                "\nSwagger 사용 방법 \n" +
                "1. 플레이어 API에서 로그인 \n" +
                "2. 로그인 응답값에서 body 값(토큰)을 복사하세요. \n" +
                "3. 우측 상단 'Authorize'를 클릭하세요. \n" +
                "4. 'Value'에 '{복사한 토큰}'을 입력하세요. \n" +
                "5. 로그인을 제외한 API를 테스트 하세요."
                , version = "v1"
        )
)
@Configuration
@Profile({"prod", "dev"})
public class SwaggerConfig {

        @Bean
        public GroupedOpenApi firstOpenApi() {
                String[] paths = {
                        "com.vsquad.iroas"
                };

                return GroupedOpenApi
                        .builder()
                        .group("iroas swagger")
                        .packagesToScan(paths)
                        .build();
        }

        @Bean
        public OpenAPI openAPI() {

                // SecuritySecheme명
                String jwtSchemeName = "jwtAuth";

                // API 요청 헤더에 인증정보 포함
                SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
                // SecuritySchemes 등록
                Components components = new Components()
                        .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                                .name(jwtSchemeName)
                                .type(SecurityScheme.Type.HTTP) // HTTP 방식
                                .scheme("bearer")
                                .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

                return new OpenAPI()
                        .addSecurityItem(securityRequirement)
                        .components(components);
        }
}
