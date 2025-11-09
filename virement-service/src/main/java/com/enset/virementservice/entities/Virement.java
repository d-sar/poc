package com.enset.virementservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "virements")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Virement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long beneficiaireId;

    private String ribSource;
    private BigDecimal montant;

    private String description;

    @Column(name = "date_virement")
    private LocalDateTime dateVirement;

    @Enumerated(EnumType.STRING)
    private TypeVirement type;

    @Enumerated(EnumType.STRING)
    private StatutVirement statut;

    public enum TypeVirement {
        NORMAL, INSTANTANE
    }

    public enum StatutVirement {
        INITIE, VALIDE, EXECUTE, REJETE, ANNULE
    }
}
