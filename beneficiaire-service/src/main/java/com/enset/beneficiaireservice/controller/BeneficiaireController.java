package com.enset.beneficiaireservice.controller;



import com.enset.beneficiaireservice.entities.Beneficiaire;
import com.enset.beneficiaireservice.repo.BeneficiaireRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/beneficiaires")
@CrossOrigin(origins = "*")
@Tag(name = "Gestion des Bénéficiaires", description = "API pour la gestion des bénéficiaires de virements")
public class BeneficiaireController {

    @Autowired
    private BeneficiaireRepository beneficiaireRepository;

    @GetMapping
    @Operation(summary = "Lister tous les bénéficiaires")
    public List<Beneficiaire> getAllBeneficiaires() {
        return beneficiaireRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouver un bénéficiaire par ID")
    public ResponseEntity<Beneficiaire> getBeneficiaireById(@PathVariable Long id) {
        Optional<Beneficiaire> beneficiaire = beneficiaireRepository.findById(id);
        return beneficiaire.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau bénéficiaire")
    public Beneficiaire createBeneficiaire(@RequestBody Beneficiaire beneficiaire) {
        return beneficiaireRepository.save(beneficiaire);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un bénéficiaire")
    public ResponseEntity<Beneficiaire> updateBeneficiaire(@PathVariable Long id,
                                                           @RequestBody Beneficiaire beneficiaireDetails) {
        Optional<Beneficiaire> optionalBeneficiaire = beneficiaireRepository.findById(id);
        if (optionalBeneficiaire.isPresent()) {
            Beneficiaire beneficiaire = optionalBeneficiaire.get();
            beneficiaire.setNom(beneficiaireDetails.getNom());
            beneficiaire.setPrenom(beneficiaireDetails.getPrenom());
            beneficiaire.setRib(beneficiaireDetails.getRib());
            beneficiaire.setType(beneficiaireDetails.getType());
            return ResponseEntity.ok(beneficiaireRepository.save(beneficiaire));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un bénéficiaire")
    public ResponseEntity<?> deleteBeneficiaire(@PathVariable Long id) {
        if (beneficiaireRepository.existsById(id)) {
            beneficiaireRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkBeneficiaireExists(@PathVariable Long id) {
        boolean exists = beneficiaireRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }

}