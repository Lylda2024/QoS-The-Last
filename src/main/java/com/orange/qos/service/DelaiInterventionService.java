package com.orange.qos.service;

import com.orange.qos.domain.DelaiIntervention;
import com.orange.qos.domain.enumeration.StatutDelai;
import com.orange.qos.repository.DelaiInterventionRepository;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.mapper.DelaiInterventionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public String calculerEtatCouleur(Instant dateLimite, StatutDelai statut) {
        if (statut == StatutDelai.TERMINE) return "GRIS";
        if (dateLimite == null) return "BLANC";

        Instant now = Instant.now();

        if (dateLimite.isBefore(now)) {
            return "ROUGE";
        }

        long joursRestants = ChronoUnit.DAYS.between(now, dateLimite);
        if (joursRestants <= 2) {
            return "JAUNE";
        }

        return "VERT";
    }

    public DelaiInterventionDTO save(DelaiInterventionDTO dto) {
        LOG.debug("Request to save DelaiIntervention : {}", dto);
        DelaiIntervention entity = delaiInterventionMapper.toEntity(dto);
        entity = delaiInterventionRepository.save(entity);
        DelaiInterventionDTO result = delaiInterventionMapper.toDto(entity);
        result.setEtatCouleur(calculerEtatCouleur(entity.getDateLimite(), entity.getStatut()));
        return result;
    }

    public DelaiInterventionDTO update(DelaiInterventionDTO dto) {
        LOG.debug("Request to update DelaiIntervention : {}", dto);
        DelaiIntervention entity = delaiInterventionMapper.toEntity(dto);
        entity = delaiInterventionRepository.save(entity);
        DelaiInterventionDTO result = delaiInterventionMapper.toDto(entity);
        result.setEtatCouleur(calculerEtatCouleur(entity.getDateLimite(), entity.getStatut()));
        return result;
    }

    public Optional<DelaiInterventionDTO> partialUpdate(DelaiInterventionDTO dto) {
        LOG.debug("Request to partially update DelaiIntervention : {}", dto);
        return delaiInterventionRepository
            .findById(dto.getId())
            .map(existing -> {
                delaiInterventionMapper.partialUpdate(existing, dto);
                return existing;
            })
            .map(delaiInterventionRepository::save)
            .map(entity -> {
                DelaiInterventionDTO result = delaiInterventionMapper.toDto(entity);
                result.setEtatCouleur(calculerEtatCouleur(entity.getDateLimite(), entity.getStatut()));
                return result;
            });
    }

    @Transactional(readOnly = true)
    public Optional<DelaiInterventionDTO> findOne(Long id) {
        LOG.debug("Request to get DelaiIntervention : {}", id);
        return delaiInterventionRepository
            .findById(id)
            .map(entity -> {
                DelaiInterventionDTO dto = delaiInterventionMapper.toDto(entity);
                dto.setEtatCouleur(calculerEtatCouleur(entity.getDateLimite(), entity.getStatut()));
                return dto;
            });
    }

    public void delete(Long id) {
        LOG.debug("Request to delete DelaiIntervention : {}", id);
        delaiInterventionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DelaiInterventionDTO> findDelaisByDegradationId(Long degradationId) {
        LOG.debug("Request to get DelaisIntervention by degradation id : {}", degradationId);
        return delaiInterventionRepository
            .findByDegradationId(degradationId)
            .stream()
            .map(entity -> {
                DelaiInterventionDTO dto = delaiInterventionMapper.toDto(entity);
                dto.setEtatCouleur(calculerEtatCouleur(entity.getDateLimite(), entity.getStatut()));
                return dto;
            })
            .collect(Collectors.toList());
    }
}
