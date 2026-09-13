package br.edu.fatecsjc.lgnspringapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ModalMapperConfigTests {
        @Autowired
        private ModelMapper modelMapper;

        @Test
        @DisplayName("Should configure ModelMapper with correct settings")
        void testModelMapperConfiguration() {
                assertNotNull(modelMapper, "ModelMapper should be injected");

                var config = modelMapper.getConfiguration();

                assertTrue(config.isFieldMatchingEnabled(),
                                "Field matching should be enabled");

                assertEquals(AccessLevel.PRIVATE, config.getFieldAccessLevel(),
                                "Field access level should be PRIVATE");

                assertEquals(NamingConventions.JAVABEANS_MUTATOR, config.getSourceNamingConvention(),
                                "Source naming convention should be JAVABEANS_MUTATOR");
        }
}