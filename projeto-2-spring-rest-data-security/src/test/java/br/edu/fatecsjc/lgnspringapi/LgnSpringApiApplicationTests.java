package br.edu.fatecsjc.lgnspringapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@ActiveProfiles("test")
class LgnSpringApiApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should load");
    }

    @Test
    void mainMethodRunsWithoutErrors() {
        assertDoesNotThrow(() -> {
            System.setProperty("spring.profiles.active", "test");
            LgnSpringApiApplication.main(new String[] {});
        });
    }
}