package com.orange.qos.service.mapper;

import com.orange.qos.domain.Role;
import com.orange.qos.domain.TypeUtilisateur;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.service.dto.RoleDTO;
import com.orange.qos.service.dto.TypeUtilisateurDTO;
import com.orange.qos.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    // DTO → Entity
    @Mapping(target = "typeUtilisateur", source = "typeUtilisateur", qualifiedByName = "mapTypeUtilisateurFromDTO")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesFromDTOs")
    @Mapping(target = "removeRoles", ignore = true)
    Utilisateur toEntity(UtilisateurDTO dto);

    // Entity → DTO
    @Mapping(target = "typeUtilisateur", source = "typeUtilisateur", qualifiedByName = "typeUtilisateurId")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleIdSet")
    UtilisateurDTO toDto(Utilisateur s);

    // Simplified DTO from Entity (only id)
    @Named("typeUtilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeUtilisateurDTO toDtoTypeUtilisateurId(TypeUtilisateur typeUtilisateur);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);

    @Named("roleIdSet")
    default Set<RoleDTO> toDtoRoleIdSet(Set<Role> roles) {
        return roles.stream().map(this::toDtoRoleId).collect(Collectors.toSet());
    }

    // Custom mapping for DTO → Entity (by id only)
    @Named("mapTypeUtilisateurFromDTO")
    default TypeUtilisateur mapTypeUtilisateurFromDTO(TypeUtilisateurDTO dto) {
        if (dto == null || dto.getId() == null) return null;
        TypeUtilisateur tu = new TypeUtilisateur();
        tu.setId(dto.getId());
        return tu;
    }

    @Named("mapRolesFromDTOs")
    default Set<Role> mapRolesFromDTOs(Set<RoleDTO> dtos) {
        if (dtos == null) return null;
        return dtos
            .stream()
            .filter(r -> r.getId() != null)
            .map(r -> {
                Role role = new Role();
                role.setId(r.getId());
                return role;
            })
            .collect(Collectors.toSet());
    }
}
