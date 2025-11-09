package com.enset.virementservice.service;

import com.enset.virementservice.client.BeneficiaireClient;
import com.enset.virementservice.dtos.BeneficiaireResponse;
import com.enset.virementservice.dtos.VirementRequest;
import com.enset.virementservice.dtos.VirementResponse;
import com.enset.virementservice.entities.Virement;
import com.enset.virementservice.repo.VirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VirementService {

    @Autowired
    private VirementRepository virementRepository;

    @Autowired
    private BeneficiaireClient beneficiaireClient;

    public VirementResponse createVirement(VirementRequest request) {
        // Validation synchrone : Vérifier que le bénéficiaire existe
        Boolean beneficiaireExists = beneficiaireClient.checkBeneficiaireExists(request.getBeneficiaireId());

        if (Boolean.FALSE.equals(beneficiaireExists)) {
            throw new IllegalArgumentException("Le bénéficiaire avec ID " + request.getBeneficiaireId() + " n'existe pas");
        }

        // Validation métier supplémentaire
        validateVirement(request);

        // Créer l'entité Virement
        Virement virement = new Virement();
        virement.setBeneficiaireId(request.getBeneficiaireId());
        virement.setRibSource(request.getRibSource());
        virement.setMontant(request.getMontant());
        virement.setDescription(request.getDescription());
        virement.setType(request.getType());
        virement.setStatut(Virement.StatutVirement.VALIDE);

        // Sauvegarder le virement
        Virement savedVirement = virementRepository.save(virement);

        return mapToResponse(savedVirement);
    }

    public VirementResponse getVirementWithDetails(Long virementId) {
        Virement virement = virementRepository.findById(virementId)
                .orElseThrow(() -> new IllegalArgumentException("Virement non trouvé avec ID: " + virementId));

        VirementResponse response = mapToResponse(virement);

        // Enrichir avec les détails du bénéficiaire
        try {
            BeneficiaireResponse beneficiaire =
                    beneficiaireClient.getBeneficiaireById(virement.getBeneficiaireId());

            response.setBeneficiaireNom(beneficiaire.getNom());
            response.setBeneficiairePrenom(beneficiaire.getPrenom());
            response.setBeneficiaireRib(beneficiaire.getRib());
        } catch (Exception e) {
            // Log l'erreur mais ne pas bloquer la réponse
            System.err.println("Erreur lors de la récupération des détails du bénéficiaire: " + e.getMessage());
        }

        return response;
    }

    public List<VirementResponse> getVirementsByBeneficiaire(Long beneficiaireId) {
        List<Virement> virements = virementRepository.findByBeneficiaireId(beneficiaireId);
        return virements.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VirementResponse> getVirementsByRibSource(String ribSource) {
        List<Virement> virements = virementRepository.findByRibSource(ribSource);
        return virements.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VirementResponse> getVirementsByStatut(Virement.StatutVirement statut) {
        List<Virement> virements = virementRepository.findByStatut(statut);
        return virements.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VirementResponse updateStatutVirement(Long virementId, Virement.StatutVirement nouveauStatut) {
        Virement virement = virementRepository.findById(virementId)
                .orElseThrow(() -> new IllegalArgumentException("Virement non trouvé avec ID: " + virementId));

        virement.setStatut(nouveauStatut);
        Virement updatedVirement = virementRepository.save(virement);

        return mapToResponse(updatedVirement);
    }

    public BigDecimal getTotalVirementsPeriod(String ribSource, LocalDateTime startDate, LocalDateTime endDate) {
        return virementRepository.getTotalVirementsByRibSourceAndPeriod(ribSource, startDate, endDate);
    }

    public void annulerVirement(Long virementId) {
        Virement virement = virementRepository.findById(virementId)
                .orElseThrow(() -> new IllegalArgumentException("Virement non trouvé avec ID: " + virementId));

        if (virement.getStatut() != Virement.StatutVirement.INITIE &&
                virement.getStatut() != Virement.StatutVirement.VALIDE) {
            throw new IllegalStateException("Impossible d'annuler un virement avec le statut: " + virement.getStatut());
        }

        virement.setStatut(Virement.StatutVirement.ANNULE);
        virementRepository.save(virement);
    }

    private void validateVirement(VirementRequest request) {
        // Validations métier
        if (request.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        // Limite de virement pour les types instantanés
        if (request.getType() == Virement.TypeVirement.INSTANTANE &&
                request.getMontant().compareTo(new BigDecimal("5000")) > 0) {
            throw new IllegalArgumentException("La limite pour les virements instantanés est de 5000");
        }

        // Validation du RIB source (format basique)
        if (request.getRibSource() == null || request.getRibSource().length() < 10) {
            throw new IllegalArgumentException("Le RIB source est invalide");
        }
    }

    private VirementResponse mapToResponse(Virement virement) {
        VirementResponse response = new VirementResponse();
        response.setId(virement.getId());
        response.setBeneficiaireId(virement.getBeneficiaireId());
        response.setRibSource(virement.getRibSource());
        response.setMontant(virement.getMontant());
        response.setDescription(virement.getDescription());
        response.setDateVirement(virement.getDateVirement());
        response.setType(virement.getType());
        response.setStatut(virement.getStatut());

        return response;
    }
}
