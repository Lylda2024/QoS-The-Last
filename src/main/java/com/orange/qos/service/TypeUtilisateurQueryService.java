package com.orange.qos.service;

import com.orange.qos.domain.*; // for static metamodels
import com.orange.qos.domain.TypeUtilisateur;
import com.orange.qos.repository.TypeUtilisateurRepository;
import com.orange.qos.service.criteria.TypeUtilisateurCriteria;
import com.orange.qos.service.dto.TypeUtilisateurDTO;
import com.orange.qos.service.mapper.TypeUtilisateurMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TypeUtilisateur} entities in the database.
 * The main input is a {@link TypeUtilisateurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeUtilisateurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeUtilisateurQueryService extends QueryService<TypeUtilisateur> {

    private static final Logger LOG = LoggerFactory.getLogger(TypeUtilisateurQueryService.class);

    private final TypeUtilisateurRepository typeUtilisateurRepository;

    private final TypeUtilisateurMapper typeUtilisateurMapper;

    public TypeUtilisateurQueryService(TypeUtilisateurRepository typeUtilisateurRepository, TypeUtilisateurMapper typeUtilisateurMapper) {
        this.typeUtilisateurRepository = typeUtilisateurRepository;
        this.typeUtilisateurMapper = typeUtilisateurMapper;
    }

    /**
     * Return a {@link List} of {@link TypeUtilisateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeUtilisateurDTO> findByCriteria(TypeUtilisateurCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TypeUtilisateur> specification = createSpecification(criteria);
        return typeUtilisateurMapper.toDto(typeUtilisateurRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeUtilisateurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TypeUtilisateur> specification = createSpecification(criteria);
        return typeUtilisateurRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeUtilisateurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeUtilisateur> createSpecification(TypeUtilisateurCriteria criteria) {
        Specification<TypeUtilisateur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TypeUtilisateur_.id),
                buildStringSpecification(criteria.getNom(), TypeUtilisateur_.nom),
                buildStringSpecification(criteria.getDescription(), TypeUtilisateur_.description),
                buildRangeSpecification(criteria.getNiveau(), TypeUtilisateur_.niveau),
                buildStringSpecification(criteria.getPermissions(), TypeUtilisateur_.permissions)
            );
        }
        return specification;
    }
}
