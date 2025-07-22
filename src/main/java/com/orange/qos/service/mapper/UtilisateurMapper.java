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
@Mapper(componentModel = "spring", uses = { TypeUtilisateurMapper.class, RoleMapper.class })
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "typeUtilisateur", source = "typeUtilisateur")
    @Mapping(target = "roles", source = "roles")
    UtilisateurDTO toDto(Utilisateur utilisateur);

    @Mapping(target = "typeUtilisateur", source = "typeUtilisateur")
    @Mapping(target = "roles", source = "roles")
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Named("typeUtilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "niveau", source = "niveau")
    @Mapping(target = "permissions", source = "permissions")
    TypeUtilisateurDTO toDtoTypeUtilisateurId(TypeUtilisateur typeUtilisateur);

    @Named("typeUtilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeUtilisateur toEntityTypeUtilisateurId(TypeUtilisateurDTO typeUtilisateurDTO);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "description", source = "description")
    RoleDTO toDtoRoleId(Role role);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Role toEntityRoleId(RoleDTO roleDTO);

    @Named("roleSet")
    default Set<RoleDTO> toDtoRoleSet(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream().map(this::toDtoRoleId).collect(Collectors.toSet());
    }

    @Named("roleSet")
    default Set<Role> toEntityRoleSet(Set<RoleDTO> roleDTOs) {
        if (roleDTOs == null) {
            return null;
        }
        return roleDTOs.stream().map(this::toEntityRoleId).collect(Collectors.toSet());
    }

    // Méthodes utilitaires pour mapper partiellement
    @AfterMapping
    default void linkRoles(@MappingTarget Utilisateur utilisateur) {
        if (utilisateur.getRoles() != null) {
            utilisateur.getRoles().forEach(role -> role.getUtilisateurs().add(utilisateur));
        }
    }
}
