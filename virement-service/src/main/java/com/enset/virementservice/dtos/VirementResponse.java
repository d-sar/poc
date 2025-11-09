package com.enset.virementservice.dtos;

import com.enset.virementservice.entities.Virement;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class VirementResponse {
    private Long id;
    private Long beneficiaireId;
    private String ribSource;
    private BigDecimal montant;
    private String description;
    private LocalDateTime dateVirement;
    private Virement.TypeVirement type;
    private Virement.StatutVirement statut;
    private String beneficiaireNom;
    private String beneficiairePrenom;
    private String beneficiaireRib;

}