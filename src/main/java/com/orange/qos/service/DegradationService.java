package com.orange.qos.service;

import com.orange.qos.domain.Degradation;
import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.mapper.DegradationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.orange.qos.domain.Degradation}.
 */
@Service
@Transactional
public class DegradationService {

    private static final Logger LOG = LoggerFactory.getLogger(DegradationService.class);

    private final DegradationRepository degradationRepository;

    private final DegradationMapper degradationMapper;

    public DegradationService(DegradationRepository degradationRepository, DegradationMapper degradationMapper) {
        this.degradationRepository = degradationRepository;
        this.degradationMapper = degradationMapper;
    }

    /**
     * Save a degradation.
     *
     * @param degradationDTO the entity to save.
     * @return the persisted entity.
     */
    public DegradationDTO save(DegradationDTO degradationDTO) {
        LOG.debug("Request to save Degradation : {}", degradationDTO);
        Degradation degradation = degradationMapper.toEntity(degradationDTO);
        degradation = degradationRepository.save(degradation);
        return degradationMapper.toDto(degradation);
    }

    /**
     * Update a degradation.
     *
     * @param degradationDTO the entity to save.
     * @return the persisted entity.
     */
    public DegradationDTO update(DegradationDTO degradationDTO) {
        LOG.debug("Request to update Degradation : {}", degradationDTO);
        Degradation degradation = degradationMapper.toEntity(degradationDTO);
        degradation = degradationRepository.save(degradation);
        return degradationMapper.toDto(degradation);
    }

    /**
     * Partially update a degradation.
     *
     * @param degradationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DegradationDTO> partialUpdate(DegradationDTO degradationDTO) {
        LOG.debug("Request to partially update Degradation : {}", degradationDTO);

        return degradationRepository
            .findById(degradationDTO.getId())
            .map(existingDegradation -> {
                degradationMapper.partialUpdate(existingDegradation, degradationDTO);

                return existingDegradation;
            })
            .map(degradationRepository::save)
            .map(degradationMapper::toDto);
    }

    /**
     * Get one degradation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DegradationDTO> findOne(Long id) {
        LOG.debug("Request to get Degradation : {}", id);
        return degradationRepository.findById(id).map(degradationMapper::toDto);
    }

    /**
     * Delete the degradation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Degradation : {}", id);
        degradationRepository.deleteById(id);
    }
}
