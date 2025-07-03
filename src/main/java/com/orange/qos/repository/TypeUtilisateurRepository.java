package com.orange.qos.repository;

import com.orange.qos.domain.TypeUtilisateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeUtilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeUtilisateurRepository extends JpaRepository<TypeUtilisateur, Long>, JpaSpecificationExecutor<TypeUtilisateur> {}
