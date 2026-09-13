package br.edu.fatecsjc.lgnspringapi.config;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import br.edu.fatecsjc.lgnspringapi.entity.Token;
import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import br.edu.fatecsjc.lgnspringapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class JwtAuthenticationFilterIntegrationTests {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_whenPathContainsAuth_thenChainCalled() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getServletPath()).thenReturn("/auth/login");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService, tokenRepository);
    }

    @Test
    void testDoFilterInternal_whenNoAuthHeader_thenChainCalled() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    void testDoFilterInternal_whenAuthHeaderNotBearer_thenChainCalled() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Basic some_credentials");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    void testDoFilterInternal_whenInvalidToken_thenChainCalledWithoutAuthentication()
            throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Nenhuma autenticação deve ser definida para token inválido");
    }

    @Test
    void testDoFilterInternal_whenValidToken_thenAuthenticationIsSet() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        String token = "validToken";
        String userEmail = "user@example.com";
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        Token validDbToken = mock(Token.class);
        when(validDbToken.isExpired()).thenReturn(false);
        when(validDbToken.isRevoked()).thenReturn(false);
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.of(validDbToken));
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "A autenticação deve ser definida");
        assertTrue(
                SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken,
                "A autenticação deve ser instância de UsernamePasswordAuthenticationToken");
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenAuthenticationAlreadyExists_thenChainCalledWithoutRecreatingAuthentication()
            throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        UserDetails dummyUser = mock(UserDetails.class);
        UsernamePasswordAuthenticationToken existingAuth = new UsernamePasswordAuthenticationToken(dummyUser, null,
                dummyUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(existingAuth);
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("dummy@example.com");
        when(userDetailsService.loadUserByUsername("dummy@example.com")).thenReturn(dummyUser);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(existingAuth, SecurityContextHolder.getContext().getAuthentication(),
                "A autenticação já existente não deve ser substituída.");
    }

    @Test
    void testDoFilterInternal_whenTokenNotValid_innerConditionFails_thenNoAuthenticationSet()
            throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer notValidToken");
        String token = "notValidToken";
        String userEmail = "user@example.com";
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Nenhuma autenticação deve existir antes da execução");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        Token invalidToken = mock(Token.class);
        when(invalidToken.isExpired()).thenReturn(true);
        when(invalidToken.isRevoked()).thenReturn(false);
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.of(invalidToken));
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Não deve definir autenticação se a validação global do token falhar.");
    }

    @Test
    void testDoFilterInternal_whenTokenNotFoundInRepository_thenNoAuthenticationSet()
            throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer tokenNotFound");

        String token = "tokenNotFound";
        String userEmail = "someone@example.com";
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.empty());
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Não deve definir autenticação se o token não for encontrado no repositório.");
    }

    @Test
    void testDoFilterInternal_whenTokenRevoked_thenNoAuthenticationSet() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer tokenRevoked");

        String token = "tokenRevoked";
        String userEmail = "revoked@example.com";

        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        Token revokedToken = mock(Token.class);
        when(revokedToken.isExpired()).thenReturn(false);
        when(revokedToken.isRevoked()).thenReturn(true);
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.of(revokedToken));
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Não deve definir autenticação se o token estiver revogado.");
    }

    @Test
    void testDoFilterInternal_whenJwtServiceReturnsFalse_thenNoAuthenticationSet()
            throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer tokenA");
        String token = "tokenA";
        String userEmail = "user@example.com";
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        Token validDbToken = mock(Token.class);
        when(validDbToken.isExpired()).thenReturn(false);
        when(validDbToken.isRevoked()).thenReturn(false);
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.of(validDbToken));
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "A autenticação não deve ser definida se jwtService.isTokenValid retornar false.");
    }

}