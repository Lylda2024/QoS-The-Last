package com.orange.qos.service.criteria;

import com.orange.qos.domain.enumeration.StatutDelai;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.orange.qos.domain.DelaiIntervention} entity. This class is used
 * in {@link com.orange.qos.web.rest.DelaiInterventionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /delai-interventions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DelaiInterventionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutDelai
     */
    public static class StatutDelaiFilter extends Filter<StatutDelai> {

        public StatutDelaiFilter() {}

        public StatutDelaiFilter(StatutDelaiFilter filter) {
            super(filter);
        }

        @Override
        public StatutDelaiFilter copy() {
            return new StatutDelaiFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateDebut;

    private InstantFilter dateLimite;

    private StringFilter commentaire;

    private StatutDelaiFilter statut;

    private LongFilter degradationId;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public DelaiInterventionCriteria() {}

    public DelaiInterventionCriteria(DelaiInterventionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(InstantFilter::copy).orElse(null);
        this.dateLimite = other.optionalDateLimite().map(InstantFilter::copy).orElse(null);
        this.commentaire = other.optionalCommentaire().map(StringFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutDelaiFilter::copy).orElse(null);
        this.degradationId = other.optionalDegradationId().map(LongFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DelaiInterventionCriteria copy() {
        return new DelaiInterventionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateDebut() {
        return dateDebut;
    }

    public Optional<InstantFilter> optionalDateDebut() {
        return Optional.ofNullable(dateDebut);
    }

    public InstantFilter dateDebut() {
        if (dateDebut == null) {
            setDateDebut(new InstantFilter());
        }
        return dateDebut;
    }

    public void setDateDebut(InstantFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public InstantFilter getDateLimite() {
        return dateLimite;
    }

    public Optional<InstantFilter> optionalDateLimite() {
        return Optional.ofNullable(dateLimite);
    }

    public InstantFilter dateLimite() {
        if (dateLimite == null) {
            setDateLimite(new InstantFilter());
        }
        return dateLimite;
    }

    public void setDateLimite(InstantFilter dateLimite) {
        this.dateLimite = dateLimite;
    }

    public StringFilter getCommentaire() {
        return commentaire;
    }

    public Optional<StringFilter> optionalCommentaire() {
        return Optional.ofNullable(commentaire);
    }

    public StringFilter commentaire() {
        if (commentaire == null) {
            setCommentaire(new StringFilter());
        }
        return commentaire;
    }

    public void setCommentaire(StringFilter commentaire) {
        this.commentaire = commentaire;
    }

    public StatutDelaiFilter getStatut() {
        return statut;
    }

    public Optional<StatutDelaiFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutDelaiFilter statut() {
        if (statut == null) {
            setStatut(new StatutDelaiFilter());
        }
        return statut;
    }

    public void setStatut(StatutDelaiFilter statut) {
        this.statut = statut;
    }

    public LongFilter getDegradationId() {
        return degradationId;
    }

    public Optional<LongFilter> optionalDegradationId() {
        return Optional.ofNullable(degradationId);
    }

    public LongFilter degradationId() {
        if (degradationId == null) {
            setDegradationId(new LongFilter());
        }
        return degradationId;
    }

    public void setDegradationId(LongFilter degradationId) {
        this.degradationId = degradationId;
    }

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public Optional<LongFilter> optionalUtilisateurId() {
        return Optional.ofNullable(utilisateurId);
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            setUtilisateurId(new LongFilter());
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DelaiInterventionCriteria that = (DelaiInterventionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateLimite, that.dateLimite) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(degradationId, that.degradationId) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateDebut, dateLimite, commentaire, statut, degradationId, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DelaiInterventionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateLimite().map(f -> "dateLimite=" + f + ", ").orElse("") +
            optionalCommentaire().map(f -> "commentaire=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDegradationId().map(f -> "degradationId=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
