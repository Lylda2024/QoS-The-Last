package com.orange.qos.service;

import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.enumeration.StatutDelai;
import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.mapper.DegradationMapper;
import jakarta.validation.Valid;
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
public class DegradationService {

    private static final Logger LOG = LoggerFactory.getLogger(DegradationService.class);

    private final DegradationRepository degradationRepository;
    private final DegradationMapper degradationMapper;
    private final DelaiInterventionService delaiInterventionService;

    public DegradationService(
        DegradationRepository degradationRepository,
        DegradationMapper degradationMapper,
        DelaiInterventionService delaiInterventionService
    ) {
        this.degradationRepository = degradationRepository;
        this.degradationMapper = degradationMapper;
        this.delaiInterventionService = delaiInterventionService;
    }

    /* -------------------  méthodes inchangées  ------------------- */

    public DegradationDTO save(DegradationDTO degradationDTO) {
        LOG.debug("Request to save Degradation : {}", degradationDTO);
        Degradation degradation = degradationMapper.toEntity(degradationDTO);
        degradation = degradationRepository.save(degradation);

        DelaiInterventionDTO premierDelaiDTO = creerPremierDelai(degradation);
        DelaiInterventionDTO savedDelaiDTO = delaiInterventionService.save(premierDelaiDTO);

        DegradationDTO result = degradationMapper.toDto(degradation);
        result.setDelais(List.of(savedDelaiDTO));
        return result;
    }

    private DelaiInterventionDTO creerPremierDelai(Degradation degradation) {
        DelaiInterventionDTO dto = new DelaiInterventionDTO();
        dto.setDateDebut(Instant.now());
        dto.setStatut(StatutDelai.EN_COURS);

        long days =
            switch (degradation.getPriorite()) {
                case "P1" -> 5;
                case "P2" -> 10;
                case "P3" -> 20;
                default -> 10;
            };

        dto.setDateLimite(Instant.now().plus(days, ChronoUnit.DAYS));
        dto.setCommentaire("Délai initial généré automatiquement");
        dto.setResponsable(degradation.getPorteur());
        dto.setDegradationId(degradation.getId());
        return dto;
    }

    public DegradationDTO update(DegradationDTO degradationDTO) {
        LOG.debug("Request to update Degradation : {}", degradationDTO);
        Degradation degradation = degradationMapper.toEntity(degradationDTO);
        degradation = degradationRepository.save(degradation);
        return degradationMapper.toDto(degradation);
    }

    public Optional<DegradationDTO> partialUpdate(DegradationDTO degradationDTO) {
        LOG.debug("Request to partially update Degradation : {}", degradationDTO);
        return degradationRepository
            .findById(degradationDTO.getId())
            .map(existing -> {
                degradationMapper.partialUpdate(existing, degradationDTO);
                return existing;
            })
            .map(degradationRepository::save)
            .map(degradationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<DegradationDTO> findOne(Long id) {
        LOG.debug("Request to get Degradation : {}", id);
        return degradationRepository
            .findById(id)
            .map(degradation -> {
                DegradationDTO dto = degradationMapper.toDto(degradation);
                dto.setDelais(delaiInterventionService.findAllByDegradationId(degradation.getId()));
                return dto;
            });
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Degradation : {}", id);
        degradationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DegradationDTO> findAllWithDelai() {
        LOG.debug("Request to get all Degradations enriched with delays");
        return degradationRepository
            .findAll()
            .stream()
            .map(d -> {
                DegradationDTO dto = degradationMapper.toDto(d);
                dto.setDelais(delaiInterventionService.findAllByDegradationId(d.getId()));
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DegradationDTO> findAllWithSite() {
        LOG.debug("Request to get all Degradations with Site");
        return degradationRepository.findAllWithSite().stream().map(degradationMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DelaiInterventionDTO> findDelaisByDegradationId(Long degradationId) {
        LOG.debug("Request to get delays by degradation id : {}", degradationId);
        return delaiInterventionService.findAllByDegradationId(degradationId);
    }

    public DelaiInterventionDTO updateDelai(Long degradationId, DelaiInterventionDTO dto) {
        dto.setDegradationId(degradationId);
        return delaiInterventionService.update(dto); // 1 arg only
    }

    public DelaiInterventionDTO addDelai(Long degradationId, @Valid DelaiInterventionDTO delaiDTO) {
        LOG.debug("Request to add Delai to degradation {} : {}", degradationId, delaiDTO);
        delaiDTO.setDegradationId(degradationId);
        return delaiInterventionService.save(delaiDTO);
    }
}
