package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ApiErrorDTOTests {
    @Test
    void testNoArgsConstructorAndSetters() {
        ApiErrorDTO error = new ApiErrorDTO();
        error.setMessage("Error occurred");
        Instant now = Instant.now();
        error.setTimestamp(now);

        assertEquals("Error occurred", error.getMessage());
        assertEquals(now, error.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        Instant ts = Instant.parse("2025-05-18T12:00:00Z");
        ApiErrorDTO error = new ApiErrorDTO("All error", ts);
        assertEquals("All error", error.getMessage());
        assertEquals(ts, error.getTimestamp());
    }

    @Test
    void testBuilderWithDefaultTimestamp() {
        ApiErrorDTO error = ApiErrorDTO.builder()
                .message("Builder error")
                .build();
        assertEquals("Builder error", error.getMessage());
        assertNotNull(error.getTimestamp());
        Instant now = Instant.now();
        Duration diff = Duration.between(error.getTimestamp(), now).abs();
        assertEquals(true, diff.getSeconds() < 5, "Default timestamp should be near current time");
    }

    @Test
    void testBuilderWithOverriddenTimestamp() {
        Instant customTs = Instant.parse("2025-12-25T00:00:00Z");
        ApiErrorDTO error = ApiErrorDTO.builder()
                .message("Custom error")
                .timestamp(customTs)
                .build();
        assertEquals("Custom error", error.getMessage());
        assertEquals(customTs, error.getTimestamp());
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = "{\"message\":\"Deserialized error\",\"timestamp\":\"2025-05-18T12:00:00Z\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        ApiErrorDTO error = mapper.readValue(json, ApiErrorDTO.class);
        assertEquals("Deserialized error", error.getMessage());
        assertEquals(Instant.parse("2025-05-18T12:00:00Z"), error.getTimestamp());
    }

    @Test
    void testToString() {
        Instant ts = Instant.parse("2025-05-18T12:00:00Z");
        ApiErrorDTO error = ApiErrorDTO.builder()
                .message("ToString error")
                .timestamp(ts)
                .build();
        String str = error.toString();
        assertNotNull(str);
        assertNotNull(str);
        assertEquals(true, str.contains("ToString error"), "String should contain the error message");
        assertEquals(true, str.contains(ts.toString()), "String should contain the timestamp");
    }

    @Test
    void testEqualsAndHashCode() {
        Instant ts = Instant.parse("2025-05-18T12:00:00Z");
        ApiErrorDTO error1 = ApiErrorDTO.builder()
                .message("Error")
                .timestamp(ts)
                .build();
        ApiErrorDTO error2 = ApiErrorDTO.builder()
                .message("Error")
                .timestamp(ts)
                .build();
        assertEquals(error1, error2, "Objects with same values must be equal");
        assertEquals(error1.hashCode(), error2.hashCode(), "HashCodes should be equal when objects are equal");
        error2.setMessage("Different error");
        assertNotEquals(error1, error2, "Objects should not be equal when values differ");
    }

    @Test
    void testEqualsWithNullFields() {
        Instant ts = Instant.now();
        ApiErrorDTO error1 = ApiErrorDTO.builder().message(null).timestamp(ts).build();
        ApiErrorDTO error2 = ApiErrorDTO.builder().message(null).timestamp(ts).build();
        assertEquals(error1, error2, "Two objects with the same field values should be equal");
        assertEquals(error1.hashCode(), error2.hashCode(), "HashCodes must be equal when field values are equal");
    }

    @Test
    void testEqualsWhenOneFieldIsNull() {
        ApiErrorDTO error1 = ApiErrorDTO.builder()
                .message("Error")
                .build();
        ApiErrorDTO error2 = ApiErrorDTO.builder()
                .message(null)
                .build();
        assertNotEquals(error1, error2, "Objects should not be equal when one message differs (null vs non-null)");
    }

    @Test
    void testEqualsSameInstance() {
        ApiErrorDTO error = ApiErrorDTO.builder().message("Same").build();
        assertEquals(error, error, "An object must be equal to itself");
    }

    @Test
    void testEqualsNull() {
        ApiErrorDTO error = ApiErrorDTO.builder().message("Test").build();
        assertNotEquals(null, error, "An object must not be equal to null");
    }

    @Test
    void testNotEqualsDifferentType() {
        ApiErrorDTO error = ApiErrorDTO.builder().message("Type Test").build();
        assertNotEquals("Some String", error, "ApiErrorDTO should not be equal to an object of a different type");
    }

    @Test
    void testHashCodeStability() {
        ApiErrorDTO error = ApiErrorDTO.builder().message("Stability").build();
        int hash1 = error.hashCode();
        int hash2 = error.hashCode();
        assertEquals(hash1, hash2, "hashCode should be stable across multiple invocations");
    }

    @Test
    void testCanEqual() {
        ApiErrorDTO error = ApiErrorDTO.builder().message("Test").build();
        assertEquals(true, error.canEqual(error), "canEqual should return true when comparing an object with itself");
        assertEquals(false, error.canEqual("Not an ApiErrorDTO"),
                "canEqual should return false when comparing with a different type");
        FakeApiErrorDTO fake = new FakeApiErrorDTO();
        assertEquals(false, error.equals(fake), "Equals must return false if fake.canEqual returns false");
    }

    static class FakeApiErrorDTO extends ApiErrorDTO {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }
}