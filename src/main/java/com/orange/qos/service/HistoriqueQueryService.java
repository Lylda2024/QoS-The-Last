package com.orange.qos.service;

import com.orange.qos.domain.*; // for static metamodels
import com.orange.qos.domain.Historique;
import com.orange.qos.repository.HistoriqueRepository;
import com.orange.qos.service.criteria.HistoriqueCriteria;
import com.orange.qos.service.dto.HistoriqueDTO;
import com.orange.qos.service.mapper.HistoriqueMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Historique} entities in the database.
 * The main input is a {@link HistoriqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoriqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueQueryService extends QueryService<Historique> {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueQueryService.class);

    private final HistoriqueRepository historiqueRepository;

    private final HistoriqueMapper historiqueMapper;

    public HistoriqueQueryService(HistoriqueRepository historiqueRepository, HistoriqueMapper historiqueMapper) {
        this.historiqueRepository = historiqueRepository;
        this.historiqueMapper = historiqueMapper;
    }

    /**
     * Return a {@link List} of {@link HistoriqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoriqueDTO> findByCriteria(HistoriqueCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Historique> specification = createSpecification(criteria);
        return historiqueMapper.toDto(historiqueRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Historique> specification = createSpecification(criteria);
        return historiqueRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Historique> createSpecification(HistoriqueCriteria criteria) {
        Specification<Historique> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Historique_.id),
                buildStringSpecification(criteria.getUtilisateur(), Historique_.utilisateur),
                buildStringSpecification(criteria.getSection(), Historique_.section),
                buildRangeSpecification(criteria.getHorodatage(), Historique_.horodatage),
                buildSpecification(criteria.getDegradationId(), root ->
                    root.join(Historique_.degradation, JoinType.LEFT).get(Degradation_.id)
                )
            );
        }
        return specification;
    }
}
