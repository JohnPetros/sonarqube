package br.edu.fatecsjc.lgnspringapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
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
@Table(name = "members")
public class Member {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "membersidgen", sequenceName = "members_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "membersidgen")
    private Long id;

    @Column(nullable = false)
    private String name;
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private GroupEntity group;
}
