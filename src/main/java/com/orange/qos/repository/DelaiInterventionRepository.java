package com.orange.qos.repository;

import com.orange.qos.domain.DelaiIntervention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DelaiIntervention entity.
 */
@Repository
public interface DelaiInterventionRepository extends JpaRepository<DelaiIntervention, Long>, JpaSpecificationExecutor<DelaiIntervention> {
    // Méthode pour récupérer tous les délais liés à une dégradation spécifique
    List<DelaiIntervention> findByDegradationId(Long degradationId);

    // Méthode pour récupérer le délai le plus récent (par dateDebut décroissante) pour une dégradation
    Optional<DelaiIntervention> findTopByDegradationIdOrderByDateDebutDesc(Long degradationId);
}
