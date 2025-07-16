package com.orange.qos.service.mapper;

import com.orange.qos.domain.Role;
import com.orange.qos.domain.TypeUtilisateur;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.service.dto.RoleDTO;
import com.orange.qos.service.dto.TypeUtilisateurDTO;
import com.orange.qos.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "typeUtilisateur", source = "typeUtilisateur", qualifiedByName = "typeUtilisateurId")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleIdSet")
    UtilisateurDTO toDto(Utilisateur s);

    @Mapping(target = "removeRoles", ignore = true)
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Named("typeUtilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeUtilisateurDTO toDtoTypeUtilisateurId(TypeUtilisateur typeUtilisateur);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);

    @Named("roleIdSet")
    default Set<RoleDTO> toDtoRoleIdSet(Set<Role> role) {
        return role.stream().map(this::toDtoRoleId).collect(Collectors.toSet());
    }
}
