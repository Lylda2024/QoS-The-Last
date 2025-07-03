package com.orange.qos.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.orange.qos.domain.Historique} entity. This class is used
 * in {@link com.orange.qos.web.rest.HistoriqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historiques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter utilisateur;

    private StringFilter section;

    private InstantFilter horodatage;

    private LongFilter degradationId;

    private Boolean distinct;

    public HistoriqueCriteria() {}

    public HistoriqueCriteria(HistoriqueCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.utilisateur = other.optionalUtilisateur().map(StringFilter::copy).orElse(null);
        this.section = other.optionalSection().map(StringFilter::copy).orElse(null);
        this.horodatage = other.optionalHorodatage().map(InstantFilter::copy).orElse(null);
        this.degradationId = other.optionalDegradationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HistoriqueCriteria copy() {
        return new HistoriqueCriteria(this);
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

    public StringFilter getUtilisateur() {
        return utilisateur;
    }

    public Optional<StringFilter> optionalUtilisateur() {
        return Optional.ofNullable(utilisateur);
    }

    public StringFilter utilisateur() {
        if (utilisateur == null) {
            setUtilisateur(new StringFilter());
        }
        return utilisateur;
    }

    public void setUtilisateur(StringFilter utilisateur) {
        this.utilisateur = utilisateur;
    }

    public StringFilter getSection() {
        return section;
    }

    public Optional<StringFilter> optionalSection() {
        return Optional.ofNullable(section);
    }

    public StringFilter section() {
        if (section == null) {
            setSection(new StringFilter());
        }
        return section;
    }

    public void setSection(StringFilter section) {
        this.section = section;
    }

    public InstantFilter getHorodatage() {
        return horodatage;
    }

    public Optional<InstantFilter> optionalHorodatage() {
        return Optional.ofNullable(horodatage);
    }

    public InstantFilter horodatage() {
        if (horodatage == null) {
            setHorodatage(new InstantFilter());
        }
        return horodatage;
    }

    public void setHorodatage(InstantFilter horodatage) {
        this.horodatage = horodatage;
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
        final HistoriqueCriteria that = (HistoriqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(utilisateur, that.utilisateur) &&
            Objects.equals(section, that.section) &&
            Objects.equals(horodatage, that.horodatage) &&
            Objects.equals(degradationId, that.degradationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utilisateur, section, horodatage, degradationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUtilisateur().map(f -> "utilisateur=" + f + ", ").orElse("") +
            optionalSection().map(f -> "section=" + f + ", ").orElse("") +
            optionalHorodatage().map(f -> "horodatage=" + f + ", ").orElse("") +
            optionalDegradationId().map(f -> "degradationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
