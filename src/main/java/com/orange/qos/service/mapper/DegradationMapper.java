package com.orange.qos.service.mapper;

import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.UtilisateurDTO;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DegradationMapper {
    /* ---------- DTO → Entity ---------- */
    @Mapping(target = "utilisateur", source = "utilisateur")
    @Mapping(target = "site", source = "site")
    Degradation toEntity(DegradationDTO dto);

    /* ---------- Entity → DTO ---------- */
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    @Mapping(target = "site.id", source = "site.id")
    @Mapping(target = "site.latitude", source = "site.latitude")
    @Mapping(target = "site.longitude", source = "site.longitude")
    DegradationDTO toDto(Degradation degradation);

    /* ---------- List ---------- */
    List<DegradationDTO> toDto(List<Degradation> entityList);

    /* ---------- Utilisateur minimal ---------- */
    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    /* ---------- Mise à jour partielle ---------- */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Degradation entity, DegradationDTO dto);
}
