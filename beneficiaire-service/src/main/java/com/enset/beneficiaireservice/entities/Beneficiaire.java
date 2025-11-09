package com.enset.beneficiaireservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "beneficiaires")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Beneficiaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    @Column(unique = true)
    private String rib;
    @Enumerated(EnumType.STRING)
    private TypeBeneficiaire type;

    public enum TypeBeneficiaire {
        PHYSIQUE, MORALE
    }

}