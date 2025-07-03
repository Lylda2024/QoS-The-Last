package com.orange.qos.service;

import com.orange.qos.domain.TypeUtilisateur;
import com.orange.qos.repository.TypeUtilisateurRepository;
import com.orange.qos.service.dto.TypeUtilisateurDTO;
import com.orange.qos.service.mapper.TypeUtilisateurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.orange.qos.domain.TypeUtilisateur}.
 */
@Service
@Transactional
public class TypeUtilisateurService {

    private static final Logger LOG = LoggerFactory.getLogger(TypeUtilisateurService.class);

    private final TypeUtilisateurRepository typeUtilisateurRepository;

    private final TypeUtilisateurMapper typeUtilisateurMapper;

    public TypeUtilisateurService(TypeUtilisateurRepository typeUtilisateurRepository, TypeUtilisateurMapper typeUtilisateurMapper) {
        this.typeUtilisateurRepository = typeUtilisateurRepository;
        this.typeUtilisateurMapper = typeUtilisateurMapper;
    }

    /**
     * Save a typeUtilisateur.
     *
     * @param typeUtilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeUtilisateurDTO save(TypeUtilisateurDTO typeUtilisateurDTO) {
        LOG.debug("Request to save TypeUtilisateur : {}", typeUtilisateurDTO);
        TypeUtilisateur typeUtilisateur = typeUtilisateurMapper.toEntity(typeUtilisateurDTO);
        typeUtilisateur = typeUtilisateurRepository.save(typeUtilisateur);
        return typeUtilisateurMapper.toDto(typeUtilisateur);
    }

    /**
     * Update a typeUtilisateur.
     *
     * @param typeUtilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeUtilisateurDTO update(TypeUtilisateurDTO typeUtilisateurDTO) {
        LOG.debug("Request to update TypeUtilisateur : {}", typeUtilisateurDTO);
        TypeUtilisateur typeUtilisateur = typeUtilisateurMapper.toEntity(typeUtilisateurDTO);
        typeUtilisateur = typeUtilisateurRepository.save(typeUtilisateur);
        return typeUtilisateurMapper.toDto(typeUtilisateur);
    }

    /**
     * Partially update a typeUtilisateur.
     *
     * @param typeUtilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeUtilisateurDTO> partialUpdate(TypeUtilisateurDTO typeUtilisateurDTO) {
        LOG.debug("Request to partially update TypeUtilisateur : {}", typeUtilisateurDTO);

        return typeUtilisateurRepository
            .findById(typeUtilisateurDTO.getId())
            .map(existingTypeUtilisateur -> {
                typeUtilisateurMapper.partialUpdate(existingTypeUtilisateur, typeUtilisateurDTO);

                return existingTypeUtilisateur;
            })
            .map(typeUtilisateurRepository::save)
            .map(typeUtilisateurMapper::toDto);
    }

    /**
     * Get one typeUtilisateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeUtilisateurDTO> findOne(Long id) {
        LOG.debug("Request to get TypeUtilisateur : {}", id);
        return typeUtilisateurRepository.findById(id).map(typeUtilisateurMapper::toDto);
    }

    /**
     * Delete the typeUtilisateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TypeUtilisateur : {}", id);
        typeUtilisateurRepository.deleteById(id);
    }
}
