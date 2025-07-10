package com.orange.qos.service.dto;

import com.orange.qos.domain.enumeration.StatutIntervention;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.DelaiIntervention} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DelaiInterventionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateDebut;

    @NotNull
    private Instant dateLimite;

    private String commentaire;

    private StatutIntervention statut;

    private UserDTO acteur;

    private DegradationDTO degradation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Instant dateLimite) {
        this.dateLimite = dateLimite;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public StatutIntervention getStatut() {
        return statut;
    }

    public void setStatut(StatutIntervention statut) {
        this.statut = statut;
    }

    public UserDTO getActeur() {
        return acteur;
    }

    public void setActeur(UserDTO acteur) {
        this.acteur = acteur;
    }

    public DegradationDTO getDegradation() {
        return degradation;
    }

    public void setDegradation(DegradationDTO degradation) {
        this.degradation = degradation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelaiInterventionDTO)) {
            return false;
        }

        DelaiInterventionDTO delaiInterventionDTO = (DelaiInterventionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, delaiInterventionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DelaiInterventionDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateLimite='" + getDateLimite() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", statut='" + getStatut() + "'" +
            ", acteur=" + getActeur() +
            ", degradation=" + getDegradation() +
            "}";
    }
}
