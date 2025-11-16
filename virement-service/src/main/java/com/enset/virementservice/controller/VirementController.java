package com.enset.virementservice.controller;

import com.enset.virementservice.dtos.VirementRequest;
import com.enset.virementservice.dtos.VirementResponse;
import com.enset.virementservice.entities.Virement;
import com.enset.virementservice.service.VirementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/virements")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Tag(name = "Gestion des Virements", description = "API pour la gestion des virements bancaires")
public class VirementController {

    @Autowired
    private VirementService virementService;

    @PostMapping
   @Operation(summary = "Créer un nouveau virement", description = "Crée un nouveau virement après validation du bénéficiaire")
    public ResponseEntity<VirementResponse> createVirement(@RequestBody VirementRequest virementRequest) {
        try {
            VirementResponse createdVirement = virementService.createVirement(virementRequest);
            return ResponseEntity.ok(createdVirement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Lister tous les virements")
    public List<VirementResponse> getAllVirements() {
        // Dans une vraie application, ajouter la pagination
        return virementService.getVirementsByRibSource("ALL"); // Modifier selon les besoins
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouver un virement par ID")
    public ResponseEntity<VirementResponse> getVirementById(@PathVariable Long id) {
        try {
            VirementResponse virement = virementService.getVirementWithDetails(id);
            return ResponseEntity.ok(virement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Obtenir un virement avec les détails complets du bénéficiaire")
    public ResponseEntity<VirementResponse> getVirementWithDetails(@PathVariable Long id) {
        try {
            VirementResponse virement = virementService.getVirementWithDetails(id);
            return ResponseEntity.ok(virement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/beneficiaire/{beneficiaireId}")
    @Operation(summary = "Lister les virements par bénéficiaire")
    public List<VirementResponse> getVirementsByBeneficiaire(@PathVariable Long beneficiaireId) {
        return virementService.getVirementsByBeneficiaire(beneficiaireId);
    }

    @GetMapping("/source/{ribSource}")
    @Operation(summary = "Lister les virements par RIB source")
    public List<VirementResponse> getVirementsByRibSource(@PathVariable String ribSource) {
        return virementService.getVirementsByRibSource(ribSource);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Lister les virements par statut")
    public List<VirementResponse> getVirementsByStatut(@PathVariable Virement.StatutVirement statut) {
        return virementService.getVirementsByStatut(statut);
    }

    @PutMapping("/{id}/statut")
    @Operation(summary = "Modifier le statut d'un virement")
    public ResponseEntity<VirementResponse> updateStatutVirement(
            @PathVariable Long id,
            @RequestParam Virement.StatutVirement nouveauStatut) {
        try {
            VirementResponse updatedVirement = virementService.updateStatutVirement(id, nouveauStatut);
            return ResponseEntity.ok(updatedVirement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/annuler")
    @Operation(summary = "Annuler un virement")
    public ResponseEntity<Void> annulerVirement(@PathVariable Long id) {
        try {
            virementService.annulerVirement(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats/total")
    @Operation(summary = "Obtenir le total des virements pour une période")
    public BigDecimal getTotalVirementsPeriod(
            @RequestParam String ribSource,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return virementService.getTotalVirementsPeriod(ribSource, startDate, endDate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un virement")
    public ResponseEntity<Void> deleteVirement(@PathVariable Long id) {
        try {
            // Dans une vraie application, on utiliserait un soft delete
            // Pour ce POC, suppression simple
            virementService.annulerVirement(id); // Alternative à la suppression
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
