package com.orange.qos.service;

import com.orange.qos.domain.*; // for static metamodels
import com.orange.qos.domain.DelaiIntervention;
import com.orange.qos.repository.DelaiInterventionRepository;
import com.orange.qos.service.criteria.DelaiInterventionCriteria;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.mapper.DelaiInterventionMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DelaiIntervention} entities in the database.
 * The main input is a {@link DelaiInterventionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DelaiInterventionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DelaiInterventionQueryService extends QueryService<DelaiIntervention> {

    private static final Logger LOG = LoggerFactory.getLogger(DelaiInterventionQueryService.class);

    private final DelaiInterventionRepository delaiInterventionRepository;

    private final DelaiInterventionMapper delaiInterventionMapper;

    public DelaiInterventionQueryService(
        DelaiInterventionRepository delaiInterventionRepository,
        DelaiInterventionMapper delaiInterventionMapper
    ) {
        this.delaiInterventionRepository = delaiInterventionRepository;
        this.delaiInterventionMapper = delaiInterventionMapper;
    }

    /**
     * Return a {@link Page} of {@link DelaiInterventionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DelaiInterventionDTO> findByCriteria(DelaiInterventionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DelaiIntervention> specification = createSpecification(criteria);
        return delaiInterventionRepository.findAll(specification, page).map(delaiInterventionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DelaiInterventionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DelaiIntervention> specification = createSpecification(criteria);
        return delaiInterventionRepository.count(specification);
    }

    /**
     * Function to convert {@link DelaiInterventionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DelaiIntervention> createSpecification(DelaiInterventionCriteria criteria) {
        Specification<DelaiIntervention> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DelaiIntervention_.id),
                buildRangeSpecification(criteria.getDateDebut(), DelaiIntervention_.dateDebut),
                buildRangeSpecification(criteria.getDateLimite(), DelaiIntervention_.dateLimite),
                buildStringSpecification(criteria.getCommentaire(), DelaiIntervention_.commentaire),
                buildSpecification(criteria.getStatut(), DelaiIntervention_.statut),
                buildSpecification(criteria.getActeurId(), root -> root.join(DelaiIntervention_.acteur, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getDegradationId(), root ->
                    root.join(DelaiIntervention_.degradation, JoinType.LEFT).get(Degradation_.id)
                )
            );
        }
        return specification;
    }
}
