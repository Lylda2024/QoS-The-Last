package com.orange.qos.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.Degradation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DegradationDTO implements Serializable {

    private Long id;

    @NotNull
    private String localite;

    @NotNull
    private String contactTemoin;

    @NotNull
    private String typeAnomalie;

    @NotNull
    private String priorite;

    @NotNull
    private String porteur;

    private String actionsEffectuees;

    private String statut;

    private UtilisateurDTO utilisateur;

    private SiteDTO site;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getContactTemoin() {
        return contactTemoin;
    }

    public void setContactTemoin(String contactTemoin) {
        this.contactTemoin = contactTemoin;
    }

    public String getTypeAnomalie() {
        return typeAnomalie;
    }

    public void setTypeAnomalie(String typeAnomalie) {
        this.typeAnomalie = typeAnomalie;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getPorteur() {
        return porteur;
    }

    public void setPorteur(String porteur) {
        this.porteur = porteur;
    }

    public String getActionsEffectuees() {
        return actionsEffectuees;
    }

    public void setActionsEffectuees(String actionsEffectuees) {
        this.actionsEffectuees = actionsEffectuees;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DegradationDTO)) {
            return false;
        }

        DegradationDTO degradationDTO = (DegradationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, degradationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DegradationDTO{" +
            "id=" + getId() +
            ", localite='" + getLocalite() + "'" +
            ", contactTemoin='" + getContactTemoin() + "'" +
            ", typeAnomalie='" + getTypeAnomalie() + "'" +
            ", priorite='" + getPriorite() + "'" +
            ", porteur='" + getPorteur() + "'" +
            ", actionsEffectuees='" + getActionsEffectuees() + "'" +
            ", statut='" + getStatut() + "'" +
            ", utilisateur=" + getUtilisateur() +
            ", site=" + getSite() +
            "}";
    }
}
