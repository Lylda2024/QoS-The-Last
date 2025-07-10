package com.orange.qos.service;

import com.orange.qos.domain.*; // for static metamodels
import com.orange.qos.domain.Degradation;
import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.service.criteria.DegradationCriteria;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.mapper.DegradationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Degradation} entities in the database.
 * The main input is a {@link DegradationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DegradationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DegradationQueryService extends QueryService<Degradation> {

    private static final Logger LOG = LoggerFactory.getLogger(DegradationQueryService.class);

    private final DegradationRepository degradationRepository;

    private final DegradationMapper degradationMapper;

    public DegradationQueryService(DegradationRepository degradationRepository, DegradationMapper degradationMapper) {
        this.degradationRepository = degradationRepository;
        this.degradationMapper = degradationMapper;
    }

    /**
     * Return a {@link Page} of {@link DegradationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DegradationDTO> findByCriteria(DegradationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Degradation> specification = createSpecification(criteria);
        return degradationRepository.findAll(specification, page).map(degradationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DegradationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Degradation> specification = createSpecification(criteria);
        return degradationRepository.count(specification);
    }

    /**
     * Function to convert {@link DegradationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Degradation> createSpecification(DegradationCriteria criteria) {
        Specification<Degradation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Degradation_.id),
                buildStringSpecification(criteria.getDescription(), Degradation_.description),
                buildRangeSpecification(criteria.getDateSignalement(), Degradation_.dateSignalement),
                buildStringSpecification(criteria.getStatut(), Degradation_.statut)
            );
        }
        return specification;
    }
}
