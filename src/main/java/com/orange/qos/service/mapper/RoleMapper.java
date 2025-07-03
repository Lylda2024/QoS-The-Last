package com.orange.qos.service.mapper;

import com.orange.qos.domain.Role;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.service.dto.RoleDTO;
import com.orange.qos.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Mapping(target = "utilisateurs", source = "utilisateurs", qualifiedByName = "utilisateurIdSet")
    RoleDTO toDto(Role s);

    @Mapping(target = "utilisateurs", ignore = true)
    @Mapping(target = "removeUtilisateurs", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("utilisateurIdSet")
    default Set<UtilisateurDTO> toDtoUtilisateurIdSet(Set<Utilisateur> utilisateur) {
        return utilisateur.stream().map(this::toDtoUtilisateurId).collect(Collectors.toSet());
    }
}
