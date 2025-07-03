package com.orange.qos.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.orange.qos.domain.Degradation} entity. This class is used
 * in {@link com.orange.qos.web.rest.DegradationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /degradations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DegradationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numero;

    private StringFilter localite;

    private StringFilter contactTemoin;

    private StringFilter typeAnomalie;

    private StringFilter priorite;

    private StringFilter problem;

    private StringFilter porteur;

    private StringFilter actionsEffectuees;

    private LongFilter utilisateurId;

    private LongFilter siteId;

    private Boolean distinct;

    public DegradationCriteria() {}

    public DegradationCriteria(DegradationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(StringFilter::copy).orElse(null);
        this.localite = other.optionalLocalite().map(StringFilter::copy).orElse(null);
        this.contactTemoin = other.optionalContactTemoin().map(StringFilter::copy).orElse(null);
        this.typeAnomalie = other.optionalTypeAnomalie().map(StringFilter::copy).orElse(null);
        this.priorite = other.optionalPriorite().map(StringFilter::copy).orElse(null);
        this.problem = other.optionalProblem().map(StringFilter::copy).orElse(null);
        this.porteur = other.optionalPorteur().map(StringFilter::copy).orElse(null);
        this.actionsEffectuees = other.optionalActionsEffectuees().map(StringFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.siteId = other.optionalSiteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DegradationCriteria copy() {
        return new DegradationCriteria(this);
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

    public StringFilter getNumero() {
        return numero;
    }

    public Optional<StringFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public StringFilter numero() {
        if (numero == null) {
            setNumero(new StringFilter());
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public StringFilter getLocalite() {
        return localite;
    }

    public Optional<StringFilter> optionalLocalite() {
        return Optional.ofNullable(localite);
    }

    public StringFilter localite() {
        if (localite == null) {
            setLocalite(new StringFilter());
        }
        return localite;
    }

    public void setLocalite(StringFilter localite) {
        this.localite = localite;
    }

    public StringFilter getContactTemoin() {
        return contactTemoin;
    }

    public Optional<StringFilter> optionalContactTemoin() {
        return Optional.ofNullable(contactTemoin);
    }

    public StringFilter contactTemoin() {
        if (contactTemoin == null) {
            setContactTemoin(new StringFilter());
        }
        return contactTemoin;
    }

    public void setContactTemoin(StringFilter contactTemoin) {
        this.contactTemoin = contactTemoin;
    }

    public StringFilter getTypeAnomalie() {
        return typeAnomalie;
    }

    public Optional<StringFilter> optionalTypeAnomalie() {
        return Optional.ofNullable(typeAnomalie);
    }

    public StringFilter typeAnomalie() {
        if (typeAnomalie == null) {
            setTypeAnomalie(new StringFilter());
        }
        return typeAnomalie;
    }

    public void setTypeAnomalie(StringFilter typeAnomalie) {
        this.typeAnomalie = typeAnomalie;
    }

    public StringFilter getPriorite() {
        return priorite;
    }

    public Optional<StringFilter> optionalPriorite() {
        return Optional.ofNullable(priorite);
    }

    public StringFilter priorite() {
        if (priorite == null) {
            setPriorite(new StringFilter());
        }
        return priorite;
    }

    public void setPriorite(StringFilter priorite) {
        this.priorite = priorite;
    }

    public StringFilter getProblem() {
        return problem;
    }

    public Optional<StringFilter> optionalProblem() {
        return Optional.ofNullable(problem);
    }

    public StringFilter problem() {
        if (problem == null) {
            setProblem(new StringFilter());
        }
        return problem;
    }

    public void setProblem(StringFilter problem) {
        this.problem = problem;
    }

    public StringFilter getPorteur() {
        return porteur;
    }

    public Optional<StringFilter> optionalPorteur() {
        return Optional.ofNullable(porteur);
    }

    public StringFilter porteur() {
        if (porteur == null) {
            setPorteur(new StringFilter());
        }
        return porteur;
    }

    public void setPorteur(StringFilter porteur) {
        this.porteur = porteur;
    }

    public StringFilter getActionsEffectuees() {
        return actionsEffectuees;
    }

    public Optional<StringFilter> optionalActionsEffectuees() {
        return Optional.ofNullable(actionsEffectuees);
    }

    public StringFilter actionsEffectuees() {
        if (actionsEffectuees == null) {
            setActionsEffectuees(new StringFilter());
        }
        return actionsEffectuees;
    }

    public void setActionsEffectuees(StringFilter actionsEffectuees) {
        this.actionsEffectuees = actionsEffectuees;
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

    public LongFilter getSiteId() {
        return siteId;
    }

    public Optional<LongFilter> optionalSiteId() {
        return Optional.ofNullable(siteId);
    }

    public LongFilter siteId() {
        if (siteId == null) {
            setSiteId(new LongFilter());
        }
        return siteId;
    }

    public void setSiteId(LongFilter siteId) {
        this.siteId = siteId;
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
        final DegradationCriteria that = (DegradationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(localite, that.localite) &&
            Objects.equals(contactTemoin, that.contactTemoin) &&
            Objects.equals(typeAnomalie, that.typeAnomalie) &&
            Objects.equals(priorite, that.priorite) &&
            Objects.equals(problem, that.problem) &&
            Objects.equals(porteur, that.porteur) &&
            Objects.equals(actionsEffectuees, that.actionsEffectuees) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(siteId, that.siteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numero,
            localite,
            contactTemoin,
            typeAnomalie,
            priorite,
            problem,
            porteur,
            actionsEffectuees,
            utilisateurId,
            siteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DegradationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalLocalite().map(f -> "localite=" + f + ", ").orElse("") +
            optionalContactTemoin().map(f -> "contactTemoin=" + f + ", ").orElse("") +
            optionalTypeAnomalie().map(f -> "typeAnomalie=" + f + ", ").orElse("") +
            optionalPriorite().map(f -> "priorite=" + f + ", ").orElse("") +
            optionalProblem().map(f -> "problem=" + f + ", ").orElse("") +
            optionalPorteur().map(f -> "porteur=" + f + ", ").orElse("") +
            optionalActionsEffectuees().map(f -> "actionsEffectuees=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalSiteId().map(f -> "siteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
