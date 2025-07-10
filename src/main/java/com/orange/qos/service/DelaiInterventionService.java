package com.orange.qos.service;

import com.orange.qos.domain.DelaiIntervention;
import com.orange.qos.repository.DelaiInterventionRepository;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.mapper.DelaiInterventionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.orange.qos.domain.DelaiIntervention}.
 */
@Service
@Transactional
public class DelaiInterventionService {

    private static final Logger LOG = LoggerFactory.getLogger(DelaiInterventionService.class);

    private final DelaiInterventionRepository delaiInterventionRepository;

    private final DelaiInterventionMapper delaiInterventionMapper;

    public DelaiInterventionService(
        DelaiInterventionRepository delaiInterventionRepository,
        DelaiInterventionMapper delaiInterventionMapper
    ) {
        this.delaiInterventionRepository = delaiInterventionRepository;
        this.delaiInterventionMapper = delaiInterventionMapper;
    }

    /**
     * Save a delaiIntervention.
     *
     * @param delaiInterventionDTO the entity to save.
     * @return the persisted entity.
     */
    public DelaiInterventionDTO save(DelaiInterventionDTO delaiInterventionDTO) {
        LOG.debug("Request to save DelaiIntervention : {}", delaiInterventionDTO);
        DelaiIntervention delaiIntervention = delaiInterventionMapper.toEntity(delaiInterventionDTO);
        delaiIntervention = delaiInterventionRepository.save(delaiIntervention);
        return delaiInterventionMapper.toDto(delaiIntervention);
    }

    /**
     * Update a delaiIntervention.
     *
     * @param delaiInterventionDTO the entity to save.
     * @return the persisted entity.
     */
    public DelaiInterventionDTO update(DelaiInterventionDTO delaiInterventionDTO) {
        LOG.debug("Request to update DelaiIntervention : {}", delaiInterventionDTO);
        DelaiIntervention delaiIntervention = delaiInterventionMapper.toEntity(delaiInterventionDTO);
        delaiIntervention = delaiInterventionRepository.save(delaiIntervention);
        return delaiInterventionMapper.toDto(delaiIntervention);
    }

    /**
     * Partially update a delaiIntervention.
     *
     * @param delaiInterventionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DelaiInterventionDTO> partialUpdate(DelaiInterventionDTO delaiInterventionDTO) {
        LOG.debug("Request to partially update DelaiIntervention : {}", delaiInterventionDTO);

        return delaiInterventionRepository
            .findById(delaiInterventionDTO.getId())
            .map(existingDelaiIntervention -> {
                delaiInterventionMapper.partialUpdate(existingDelaiIntervention, delaiInterventionDTO);

                return existingDelaiIntervention;
            })
            .map(delaiInterventionRepository::save)
            .map(delaiInterventionMapper::toDto);
    }

    /**
     * Get all the delaiInterventions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DelaiInterventionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return delaiInterventionRepository.findAllWithEagerRelationships(pageable).map(delaiInterventionMapper::toDto);
    }

    /**
     * Get one delaiIntervention by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DelaiInterventionDTO> findOne(Long id) {
        LOG.debug("Request to get DelaiIntervention : {}", id);
        return delaiInterventionRepository.findOneWithEagerRelationships(id).map(delaiInterventionMapper::toDto);
    }

    /**
     * Delete the delaiIntervention by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DelaiIntervention : {}", id);
        delaiInterventionRepository.deleteById(id);
    }
}
