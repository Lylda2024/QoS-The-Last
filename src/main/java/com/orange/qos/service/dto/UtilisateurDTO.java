package com.orange.qos.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.orange.qos.domain.Utilisateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurDTO implements Serializable {

    private Long id;

    @NotNull(message = "Le nom ne peut pas être null")
    @Size(min = 1, max = 50, message = "Le nom doit contenir entre 1 et 50 caractères")
    private String nom;

    @NotNull(message = "Le prénom ne peut pas être null")
    @Size(min = 1, max = 50, message = "Le prénom doit contenir entre 1 et 50 caractères")
    private String prenom;

    @NotNull(message = "L'email ne peut pas être null")
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    private String email;

    @NotNull(message = "Le mot de passe ne peut pas être null")
    @Size(min = 4, max = 100, message = "Le mot de passe doit contenir entre 4 et 100 caractères")
    private String motDePasse;

    @NotNull(message = "Le type d'utilisateur ne peut pas être null")
    private TypeUtilisateurDTO typeUtilisateur;

    private Set<RoleDTO> roles = new HashSet<>();

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public TypeUtilisateurDTO getTypeUtilisateur() {
        return typeUtilisateur;
    }

    public void setTypeUtilisateur(TypeUtilisateurDTO typeUtilisateur) {
        this.typeUtilisateur = typeUtilisateur;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurDTO)) {
            return false;
        }

        UtilisateurDTO utilisateurDTO = (UtilisateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilisateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "UtilisateurDTO{" +
            "id=" +
            getId() +
            ", nom='" +
            getNom() +
            "'" +
            ", prenom='" +
            getPrenom() +
            "'" +
            ", email='" +
            getEmail() +
            "'" +
            ", motDePasse='" +
            (getMotDePasse() != null ? "*****" : null) +
            "'" +
            ", typeUtilisateur=" +
            getTypeUtilisateur() +
            ", roles=" +
            getRoles() +
            "}"
        );
    }
}
