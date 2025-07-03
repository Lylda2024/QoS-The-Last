package com.orange.qos.service.mapper;

import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.Site;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.SiteDTO;
import com.orange.qos.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Degradation} and its DTO {@link DegradationDTO}.
 */
@Mapper(componentModel = "spring")
public interface DegradationMapper extends EntityMapper<DegradationDTO, Degradation> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    @Mapping(target = "site", source = "site", qualifiedByName = "siteId")
    DegradationDTO toDto(Degradation s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("siteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SiteDTO toDtoSiteId(Site site);
}
