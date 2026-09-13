package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.fatecsjc.lgnspringapi.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "tokensidgen", sequenceName = "tokens_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokensidgen")
    private Long id;

    @Column(name = "token", unique = true, nullable = false)
    private String tokenValue;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
