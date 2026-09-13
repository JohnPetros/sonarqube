package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "marathons")
public class Marathon {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "marathonsidgen", sequenceName = "marathons_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marathonsidgen")
    private Long id;

    private String peso;
    private String score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;
}
