package br.edu.fatecsjc.lgnspringapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Import(OpenApiConfig.class)
class OpenApiConfigTests {
    @Autowired
    private ApplicationContext applicationContext;

    @TestConfiguration
    static class OpenApiTestConfiguration {
        @Bean
        public OpenAPI openAPI() {
            return new OpenAPI()
                    .info(new io.swagger.v3.oas.models.info.Info()
                            .title("OpenApi specification - LGN")
                            .description("Projeto Semestral de Qualidade de Software")
                            .version("1.0"));
        }
    }

    @Test
    void testAnnotationsPresenceAndValues() {
        OpenAPIDefinition openAPIDefinition = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class);
        assertNotNull(openAPIDefinition, "A anotação OpenAPIDefinition deve estar presente");

        Info info = openAPIDefinition.info();
        assertNotNull(info, "A configuração Info não pode ser nula");
        assertEquals("OpenApi specification - LGN", info.title(), "Título da OpenAPI incorreto");
        assertEquals("Projeto Semestral de Qualidade de Software", info.description(), "Descrição incorreta");

        Contact contact = info.contact();
        assertNotNull(contact, "A configuração Contact não pode ser nula");
        assertEquals("Bruno Serpa Pereira Carvalho", contact.name(), "Nome de contato incorreto");
        assertEquals("brunospc2005@gmail.com", contact.email(), "Email de contato incorreto");
        assertEquals("http://brunoserpa.vercel.app/", contact.url(), "URL de contato incorreta");

        SecurityScheme securityScheme = OpenApiConfig.class.getAnnotation(SecurityScheme.class);
        assertNotNull(securityScheme, "A anotação SecurityScheme deve estar presente");
        assertEquals("bearerAuth", securityScheme.name(), "Nome do security scheme incorreto");
        assertEquals("bearer", securityScheme.scheme(), "Scheme deve ser 'bearer'");
        assertEquals("JWT", securityScheme.bearerFormat(), "Bearer format deve ser 'JWT'");
    }

    @Test
    void testOpenApiBeanLoaded() {
        OpenAPI bean = applicationContext.getBean(OpenAPI.class);
        assertNotNull(bean, "O bean OpenAPI deve estar carregado no contexto");
        assertNotNull(bean.getInfo(), "A Info do bean OpenAPI não pode ser nula");
        assertTrue(bean.getInfo().getTitle().contains("OpenApi"),
                "O título do OpenAPI deve conter 'OpenApi'");
    }

    @Test
    void testOpenApiConfigInstantiation() {
        OpenApiConfig config = new OpenApiConfig();
        assertNotNull(config, "A instância de OpenApiConfig não deve ser nula");
    }
}
