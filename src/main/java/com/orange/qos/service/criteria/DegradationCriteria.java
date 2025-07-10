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

    private StringFilter description;

    private InstantFilter dateSignalement;

    private StringFilter statut;

    private Boolean distinct;

    public DegradationCriteria() {}

    public DegradationCriteria(DegradationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.dateSignalement = other.optionalDateSignalement().map(InstantFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StringFilter::copy).orElse(null);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getDateSignalement() {
        return dateSignalement;
    }

    public Optional<InstantFilter> optionalDateSignalement() {
        return Optional.ofNullable(dateSignalement);
    }

    public InstantFilter dateSignalement() {
        if (dateSignalement == null) {
            setDateSignalement(new InstantFilter());
        }
        return dateSignalement;
    }

    public void setDateSignalement(InstantFilter dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public StringFilter getStatut() {
        return statut;
    }

    public Optional<StringFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StringFilter statut() {
        if (statut == null) {
            setStatut(new StringFilter());
        }
        return statut;
    }

    public void setStatut(StringFilter statut) {
        this.statut = statut;
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
            Objects.equals(description, that.description) &&
            Objects.equals(dateSignalement, that.dateSignalement) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, dateSignalement, statut, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DegradationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDateSignalement().map(f -> "dateSignalement=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
