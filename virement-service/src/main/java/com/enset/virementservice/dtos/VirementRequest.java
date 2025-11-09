package com.enset.virementservice.dtos;


import com.enset.virementservice.entities.Virement;
import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter @Setter
public class VirementRequest {

    private Long beneficiaireId;
    private String ribSource;
    private BigDecimal montant;

    private String description;

    private Virement.TypeVirement type;

}
