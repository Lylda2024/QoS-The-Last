package com.orange.qos.repository;

import com.orange.qos.domain.Degradation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Degradation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DegradationRepository extends JpaRepository<Degradation, Long>, JpaSpecificationExecutor<Degradation> {}
