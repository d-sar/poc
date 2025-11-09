package com.enset.beneficiaireservice.repo;

import com.enset.beneficiaireservice.entities.Beneficiaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficiaireRepository extends JpaRepository<Beneficiaire, Long> {
    Optional<Beneficiaire> findByRib(String rib);
    boolean existsByRib(String rib);
}
