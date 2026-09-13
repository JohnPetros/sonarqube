package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "addressesidgen", sequenceName = "addresses_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressesidgen")
    private Long id;

    private String logradouro;
    private String numero;
    private String bairro;
    private String cep;
    private String municipio;
    private String estado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    @JsonBackReference
    private Organization organization;
}