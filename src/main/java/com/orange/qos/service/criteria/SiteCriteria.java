package com.orange.qos.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.orange.qos.domain.Site} entity. This class is used
 * in {@link com.orange.qos.web.rest.SiteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomSite;

    private StringFilter codeOCI;

    private FloatFilter longitude;

    private FloatFilter latitude;

    private StringFilter statut;

    private StringFilter technologie;

    private BooleanFilter enService;

    private Boolean distinct;

    public SiteCriteria() {}

    public SiteCriteria(SiteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nomSite = other.optionalNomSite().map(StringFilter::copy).orElse(null);
        this.codeOCI = other.optionalCodeOCI().map(StringFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(FloatFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(FloatFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StringFilter::copy).orElse(null);
        this.technologie = other.optionalTechnologie().map(StringFilter::copy).orElse(null);
        this.enService = other.optionalEnService().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SiteCriteria copy() {
        return new SiteCriteria(this);
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

    public StringFilter getNomSite() {
        return nomSite;
    }

    public Optional<StringFilter> optionalNomSite() {
        return Optional.ofNullable(nomSite);
    }

    public StringFilter nomSite() {
        if (nomSite == null) {
            setNomSite(new StringFilter());
        }
        return nomSite;
    }

    public void setNomSite(StringFilter nomSite) {
        this.nomSite = nomSite;
    }

    public StringFilter getCodeOCI() {
        return codeOCI;
    }

    public Optional<StringFilter> optionalCodeOCI() {
        return Optional.ofNullable(codeOCI);
    }

    public StringFilter codeOCI() {
        if (codeOCI == null) {
            setCodeOCI(new StringFilter());
        }
        return codeOCI;
    }

    public void setCodeOCI(StringFilter codeOCI) {
        this.codeOCI = codeOCI;
    }

    public FloatFilter getLongitude() {
        return longitude;
    }

    public Optional<FloatFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public FloatFilter longitude() {
        if (longitude == null) {
            setLongitude(new FloatFilter());
        }
        return longitude;
    }

    public void setLongitude(FloatFilter longitude) {
        this.longitude = longitude;
    }

    public FloatFilter getLatitude() {
        return latitude;
    }

    public Optional<FloatFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public FloatFilter latitude() {
        if (latitude == null) {
            setLatitude(new FloatFilter());
        }
        return latitude;
    }

    public void setLatitude(FloatFilter latitude) {
        this.latitude = latitude;
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

    public StringFilter getTechnologie() {
        return technologie;
    }

    public Optional<StringFilter> optionalTechnologie() {
        return Optional.ofNullable(technologie);
    }

    public StringFilter technologie() {
        if (technologie == null) {
            setTechnologie(new StringFilter());
        }
        return technologie;
    }

    public void setTechnologie(StringFilter technologie) {
        this.technologie = technologie;
    }

    public BooleanFilter getEnService() {
        return enService;
    }

    public Optional<BooleanFilter> optionalEnService() {
        return Optional.ofNullable(enService);
    }

    public BooleanFilter enService() {
        if (enService == null) {
            setEnService(new BooleanFilter());
        }
        return enService;
    }

    public void setEnService(BooleanFilter enService) {
        this.enService = enService;
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
        final SiteCriteria that = (SiteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomSite, that.nomSite) &&
            Objects.equals(codeOCI, that.codeOCI) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(technologie, that.technologie) &&
            Objects.equals(enService, that.enService) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomSite, codeOCI, longitude, latitude, statut, technologie, enService, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNomSite().map(f -> "nomSite=" + f + ", ").orElse("") +
            optionalCodeOCI().map(f -> "codeOCI=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalTechnologie().map(f -> "technologie=" + f + ", ").orElse("") +
            optionalEnService().map(f -> "enService=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
