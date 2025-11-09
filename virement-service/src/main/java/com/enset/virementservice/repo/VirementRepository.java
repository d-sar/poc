package com.enset.virementservice.repo;


import com.enset.virementservice.entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VirementRepository extends JpaRepository<Virement, Long> {

    List<Virement> findByBeneficiaireId(Long beneficiaireId);

    List<Virement> findByRibSource(String ribSource);

    List<Virement> findByStatut(Virement.StatutVirement statut);

    List<Virement> findByDateVirementBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(v.montant) FROM Virement v WHERE v.ribSource = :ribSource AND v.dateVirement BETWEEN :startDate AND :endDate")
    BigDecimal getTotalVirementsByRibSourceAndPeriod(@Param("ribSource") String ribSource,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(v) FROM Virement v WHERE v.ribSource = :ribSource AND v.dateVirement BETWEEN :startDate AND :endDate")
    Long countVirementsByRibSourceAndPeriod(@Param("ribSource") String ribSource,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    List<Virement> findByType(Virement.TypeVirement type);
}
