package com.orange.qos.repository;

import com.orange.qos.domain.Degradation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface DegradationRepository extends JpaRepository<Degradation, Long>, JpaSpecificationExecutor<Degradation> {
    /** Charge les dégradations avec le site (lat / lng) en une seule requête */
    @Query("SELECT d FROM Degradation d JOIN FETCH d.site")
    List<Degradation> findAllWithSite();
}
