package com.orange.qos.repository;

import com.orange.qos.domain.DelaiIntervention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DelaiIntervention entity.
 */
@Repository
public interface DelaiInterventionRepository extends JpaRepository<DelaiIntervention, Long>, JpaSpecificationExecutor<DelaiIntervention> {
    @Query(
        "select delaiIntervention from DelaiIntervention delaiIntervention where delaiIntervention.acteur.login = ?#{authentication.name}"
    )
    List<DelaiIntervention> findByActeurIsCurrentUser();

    default Optional<DelaiIntervention> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DelaiIntervention> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DelaiIntervention> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select delaiIntervention from DelaiIntervention delaiIntervention left join fetch delaiIntervention.acteur",
        countQuery = "select count(delaiIntervention) from DelaiIntervention delaiIntervention"
    )
    Page<DelaiIntervention> findAllWithToOneRelationships(Pageable pageable);

    @Query("select delaiIntervention from DelaiIntervention delaiIntervention left join fetch delaiIntervention.acteur")
    List<DelaiIntervention> findAllWithToOneRelationships();

    @Query(
        "select delaiIntervention from DelaiIntervention delaiIntervention left join fetch delaiIntervention.acteur where delaiIntervention.id =:id"
    )
    Optional<DelaiIntervention> findOneWithToOneRelationships(@Param("id") Long id);
}
