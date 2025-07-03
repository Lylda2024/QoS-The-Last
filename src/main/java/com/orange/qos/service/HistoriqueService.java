package com.orange.qos.service;

import com.orange.qos.domain.Historique;
import com.orange.qos.repository.HistoriqueRepository;
import com.orange.qos.service.dto.HistoriqueDTO;
import com.orange.qos.service.mapper.HistoriqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.orange.qos.domain.Historique}.
 */
@Service
@Transactional
public class HistoriqueService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueService.class);

    private final HistoriqueRepository historiqueRepository;

    private final HistoriqueMapper historiqueMapper;

    public HistoriqueService(HistoriqueRepository historiqueRepository, HistoriqueMapper historiqueMapper) {
        this.historiqueRepository = historiqueRepository;
        this.historiqueMapper = historiqueMapper;
    }

    /**
     * Save a historique.
     *
     * @param historiqueDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriqueDTO save(HistoriqueDTO historiqueDTO) {
        LOG.debug("Request to save Historique : {}", historiqueDTO);
        Historique historique = historiqueMapper.toEntity(historiqueDTO);
        historique = historiqueRepository.save(historique);
        return historiqueMapper.toDto(historique);
    }

    /**
     * Update a historique.
     *
     * @param historiqueDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriqueDTO update(HistoriqueDTO historiqueDTO) {
        LOG.debug("Request to update Historique : {}", historiqueDTO);
        Historique historique = historiqueMapper.toEntity(historiqueDTO);
        historique = historiqueRepository.save(historique);
        return historiqueMapper.toDto(historique);
    }

    /**
     * Partially update a historique.
     *
     * @param historiqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoriqueDTO> partialUpdate(HistoriqueDTO historiqueDTO) {
        LOG.debug("Request to partially update Historique : {}", historiqueDTO);

        return historiqueRepository
            .findById(historiqueDTO.getId())
            .map(existingHistorique -> {
                historiqueMapper.partialUpdate(existingHistorique, historiqueDTO);

                return existingHistorique;
            })
            .map(historiqueRepository::save)
            .map(historiqueMapper::toDto);
    }

    /**
     * Get one historique by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoriqueDTO> findOne(Long id) {
        LOG.debug("Request to get Historique : {}", id);
        return historiqueRepository.findById(id).map(historiqueMapper::toDto);
    }

    /**
     * Delete the historique by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Historique : {}", id);
        historiqueRepository.deleteById(id);
    }
}
