package com.orange.qos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.TypeUtilisateur} entity.
 */
@Schema(description = "Modèle de données conforme au diagramme UML")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeUtilisateurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String description;

    private Integer niveau;

    private String permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeUtilisateurDTO)) {
            return false;
        }

        TypeUtilisateurDTO typeUtilisateurDTO = (TypeUtilisateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeUtilisateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeUtilisateurDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", niveau=" + getNiveau() +
            ", permissions='" + getPermissions() + "'" +
            "}";
    }
}
