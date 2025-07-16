package com.orange.qos.service.dto;

import com.orange.qos.domain.enumeration.StatutDelai;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class DelaiInterventionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateDebut;

    @NotNull
    private Instant dateLimite;

    private String commentaire;

    private StatutDelai statut;

    private DegradationDTO degradation;

    private UtilisateurDTO utilisateur;

    // ✅ Nouveau champ ajouté
    private String etatCouleur;

    // --- Getters & Setters ---

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

    public StatutDelai getStatut() {
        return statut;
    }

    public void setStatut(StatutDelai statut) {
        this.statut = statut;
    }

    public DegradationDTO getDegradation() {
        return degradation;
    }

    public void setDegradation(DegradationDTO degradation) {
        this.degradation = degradation;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getEtatCouleur() {
        return etatCouleur;
    }

    public void setEtatCouleur(String etatCouleur) {
        this.etatCouleur = etatCouleur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DelaiInterventionDTO)) return false;
        DelaiInterventionDTO that = (DelaiInterventionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "DelaiInterventionDTO{" +
            "id=" +
            id +
            ", dateDebut=" +
            dateDebut +
            ", dateLimite=" +
            dateLimite +
            ", commentaire='" +
            commentaire +
            '\'' +
            ", statut=" +
            statut +
            ", etatCouleur='" +
            etatCouleur +
            '\'' +
            ", degradation=" +
            degradation +
            ", utilisateur=" +
            utilisateur +
            '}'
        );
    }
}
