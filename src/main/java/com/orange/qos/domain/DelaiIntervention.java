package com.orange.qos.domain;

import com.orange.qos.domain.enumeration.StatutIntervention;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DelaiIntervention.
 */
@Entity
@Table(name = "delai_intervention")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DelaiIntervention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @NotNull
    @Column(name = "date_limite", nullable = false)
    private Instant dateLimite;

    @Column(name = "commentaire")
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutIntervention statut;

    @ManyToOne(fetch = FetchType.LAZY)
    private User acteur;

    @ManyToOne(fetch = FetchType.LAZY)
    private Degradation degradation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DelaiIntervention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public DelaiIntervention dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateLimite() {
        return this.dateLimite;
    }

    public DelaiIntervention dateLimite(Instant dateLimite) {
        this.setDateLimite(dateLimite);
        return this;
    }

    public void setDateLimite(Instant dateLimite) {
        this.dateLimite = dateLimite;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public DelaiIntervention commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public StatutIntervention getStatut() {
        return this.statut;
    }

    public DelaiIntervention statut(StatutIntervention statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutIntervention statut) {
        this.statut = statut;
    }

    public User getActeur() {
        return this.acteur;
    }

    public void setActeur(User user) {
        this.acteur = user;
    }

    public DelaiIntervention acteur(User user) {
        this.setActeur(user);
        return this;
    }

    public Degradation getDegradation() {
        return this.degradation;
    }

    public void setDegradation(Degradation degradation) {
        this.degradation = degradation;
    }

    public DelaiIntervention degradation(Degradation degradation) {
        this.setDegradation(degradation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelaiIntervention)) {
            return false;
        }
        return getId() != null && getId().equals(((DelaiIntervention) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DelaiIntervention{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateLimite='" + getDateLimite() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
