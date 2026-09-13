package br.edu.fatecsjc.lgnspringapi.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class RootResourceTests {
    private RootResource rootResource;

    @BeforeEach
    void setUp() {
        rootResource = new RootResource();
        ReflectionTestUtils.setField(rootResource, "port", "8080");
    }

    @Test
    void testValidateRestResource() {
        ResponseEntity<String> response = rootResource.validateRestResource();

        String expectedMessage = "Bem-vindo, APIs operacionais. " +
                "Acesse: <a href=\"http://localhost:8080/swagger-ui/index.html\">" +
                "http://localhost:8080/swagger-ui/index.html</a>";

        assertThat(response)
                .isNotNull()
                .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                .returns(expectedMessage, ResponseEntity::getBody);
    }
}