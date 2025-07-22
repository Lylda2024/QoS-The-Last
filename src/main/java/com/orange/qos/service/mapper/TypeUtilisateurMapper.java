package com.orange.qos.service.mapper;

import com.orange.qos.domain.TypeUtilisateur;
import com.orange.qos.service.dto.TypeUtilisateurDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link TypeUtilisateur} and its DTO {@link TypeUtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeUtilisateurMapper extends EntityMapper<TypeUtilisateurDTO, TypeUtilisateur> {
    // Aucune méthode supplémentaire nécessaire : MapStruct génère automatiquement
    // les implémentations de toDto(...) et toEntity(...) via EntityMapper.
}
