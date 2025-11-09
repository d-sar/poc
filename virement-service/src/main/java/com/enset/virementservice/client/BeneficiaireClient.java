package com.enset.virementservice.client;

import com.enset.virementservice.dtos.BeneficiaireResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "beneficiaire-service",
        path = "/beneficiaires"
)
public interface BeneficiaireClient {

    @GetMapping("/{id}")
    BeneficiaireResponse getBeneficiaireById(@PathVariable("id") Long id);

    @GetMapping("/{id}/exists")
    Boolean checkBeneficiaireExists(@PathVariable("id") Long id);

    @GetMapping("/rib/{rib}")
    BeneficiaireResponse getBeneficiaireByRib(@PathVariable("rib") String rib);


}