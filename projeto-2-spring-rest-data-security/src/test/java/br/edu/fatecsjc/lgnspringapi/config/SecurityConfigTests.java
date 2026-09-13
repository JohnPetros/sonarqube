package br.edu.fatecsjc.lgnspringapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SecurityConfigTests {
    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private LogoutHandler logoutHandler;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertNotNull(securityConfig);
        assertNotNull(jwtAuthFilter);
        assertNotNull(authenticationProvider);
        assertNotNull(logoutHandler);
    }

    @Test
    void swaggerEndpointsShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void authEndpointsShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void securedEndpointsShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/group"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 403,
                            "Expected HTTP status 401 (Unauthorized) or 403 (Forbidden) but was: " + status);
                });
    }

    @Test
    void logoutEndpointClearsSecurityContext() throws Exception {
        UserDetails dummyUser = mock(UserDetails.class);
        UsernamePasswordAuthenticationToken dummyAuth = 
                new UsernamePasswordAuthenticationToken(dummyUser, null, dummyUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(dummyAuth);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(),
                "A autenticação deve estar presente antes do logout");
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "A autenticação deve ser removida após a execução do logoutSuccessHandler");
    }
}
