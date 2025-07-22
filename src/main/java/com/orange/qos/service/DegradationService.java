package com.orange.qos.service;

import com.orange.qos.domain.Degradation;
import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.repository.DelaiInterventionRepository;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.mapper.DegradationMapper;
import com.orange.qos.service.mapper.DelaiInterventionMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final DelaiInterventionRepository delaiInterventionRepository;
    private final DelaiInterventionMapper delaiInterventionMapper;
    private final DelaiInterventionService delaiInterventionService;

    public DegradationService(
        DegradationRepository degradationRepository,
        DegradationMapper degradationMapper,
        DelaiInterventionRepository delaiInterventionRepository,
        DelaiInterventionMapper delaiInterventionMapper,
        DelaiInterventionService delaiInterventionService
    ) {
        this.degradationRepository = degradationRepository;
        this.degradationMapper = degradationMapper;
        this.delaiInterventionRepository = delaiInterventionRepository;
        this.delaiInterventionMapper = delaiInterventionMapper;
        this.delaiInterventionService = delaiInterventionService;
    }

    /**
     * Sauvegarde une dégradation simple.
     */
    public DegradationDTO save(DegradationDTO degradationDTO) {
        LOG.debug("Request to save Degradation : {}", degradationDTO);
        Degradation degradation = degradationMapper.toEntity(degradationDTO);
        degradation = degradationRepository.save(degradation);
        return degradationMapper.toDto(degradation);
    }

    /**
     * Mise à jour complète.
     */
    public DegradationDTO update(DegradationDTO degradationDTO) {
        LOG.debug("Request to update Degradation : {}", degradationDTO);
        Degradation degradation = degradationMapper.toEntity(degradationDTO);
        degradation = degradationRepository.save(degradation);
        return degradationMapper.toDto(degradation);
    }

    /**
     * Mise à jour partielle.
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
     * Récupère une dégradation avec ses délais enrichis.
     */
    @Transactional(readOnly = true)
    public Optional<DegradationDTO> findOne(Long id) {
        LOG.debug("Request to get Degradation : {}", id);

        return degradationRepository
            .findById(id)
            .map(degradation -> {
                DegradationDTO dto = degradationMapper.toDto(degradation);

                // On charge les délais associés et on les enrichit avec leur couleur
                List<DelaiInterventionDTO> delais = delaiInterventionRepository
                    .findByDegradationId(degradation.getId())
                    .stream()
                    .map(delai -> delaiInterventionMapper.toDtoWithEtatCouleur(delai))
                    .collect(Collectors.toList());

                dto.setDelais(delais);
                return dto;
            });
    }

    /**
     * Supprime une dégradation.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Degradation : {}", id);
        degradationRepository.deleteById(id);
    }

    /**
     * Récupère toutes les dégradations avec leurs délais enrichis.
     */
    @Transactional(readOnly = true)
    public List<DegradationDTO> findAllWithDelai() {
        LOG.debug("Request to get all Degradations enriched with delays");

        return degradationRepository
            .findAll()
            .stream()
            .map(degradation -> {
                DegradationDTO dto = degradationMapper.toDto(degradation);

                // On enrichit chaque délai avec son état couleur
                List<DelaiInterventionDTO> delais = delaiInterventionRepository
                    .findByDegradationId(degradation.getId())
                    .stream()
                    .map(delai -> delaiInterventionMapper.toDtoWithEtatCouleur(delai))
                    .collect(Collectors.toList());

                dto.setDelais(delais);
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * Retourne la liste des délais d'intervention liés à une dégradation,
     * avec calcul de la couleur d'état pour chaque délai.
     */
    @Transactional(readOnly = true)
    public List<DelaiInterventionDTO> findDelaisByDegradationId(Long degradationId) {
        LOG.debug("Request to get delays by degradation id : {}", degradationId);
        return delaiInterventionRepository
            .findByDegradationId(degradationId)
            .stream()
            .map(delai -> delaiInterventionMapper.toDtoWithEtatCouleur(delai))
            .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les dégradations avec leurs sites uniquement.
     */
    @Transactional(readOnly = true)
    public List<DegradationDTO> findAllWithSite() {
        LOG.debug("Request to get all Degradations with Site");
        return degradationRepository.findAllWithSite().stream().map(degradationMapper::toDto).collect(Collectors.toList());
    }
}
