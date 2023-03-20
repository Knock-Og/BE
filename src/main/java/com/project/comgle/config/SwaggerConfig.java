package com.project.comgle.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {
        // Swagger Ui 문서 상 표시할 정보들
        Info info = new Info()
                .version("v0.0.1")
                .title("사내 문서 검색 서비스 Knock \uD83D\uDECE") // 문서 제목
                .description("Knock 프로젝트 API 명세서입니다."); // 문서 설명

/*         Swagger UI에서 요청 시 Token을 Header에 담아 보내기 위한 설정
        추후 적용
        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)*/

        return new OpenAPI()
                .info(info)
                .components(new Components());
/*                .addSecurityItem(securityRequirement)
                .components(components);*/

    }
}
