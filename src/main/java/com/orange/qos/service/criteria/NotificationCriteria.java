package com.orange.qos.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.orange.qos.domain.Notification} entity. This class is used
 * in {@link com.orange.qos.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter message;

    private InstantFilter dateEnvoi;

    private BooleanFilter statutLecture;

    private LongFilter degradationId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.dateEnvoi = other.optionalDateEnvoi().map(InstantFilter::copy).orElse(null);
        this.statutLecture = other.optionalStatutLecture().map(BooleanFilter::copy).orElse(null);
        this.degradationId = other.optionalDegradationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public InstantFilter getDateEnvoi() {
        return dateEnvoi;
    }

    public Optional<InstantFilter> optionalDateEnvoi() {
        return Optional.ofNullable(dateEnvoi);
    }

    public InstantFilter dateEnvoi() {
        if (dateEnvoi == null) {
            setDateEnvoi(new InstantFilter());
        }
        return dateEnvoi;
    }

    public void setDateEnvoi(InstantFilter dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public BooleanFilter getStatutLecture() {
        return statutLecture;
    }

    public Optional<BooleanFilter> optionalStatutLecture() {
        return Optional.ofNullable(statutLecture);
    }

    public BooleanFilter statutLecture() {
        if (statutLecture == null) {
            setStatutLecture(new BooleanFilter());
        }
        return statutLecture;
    }

    public void setStatutLecture(BooleanFilter statutLecture) {
        this.statutLecture = statutLecture;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(message, that.message) &&
            Objects.equals(dateEnvoi, that.dateEnvoi) &&
            Objects.equals(statutLecture, that.statutLecture) &&
            Objects.equals(degradationId, that.degradationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, message, dateEnvoi, statutLecture, degradationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalDateEnvoi().map(f -> "dateEnvoi=" + f + ", ").orElse("") +
            optionalStatutLecture().map(f -> "statutLecture=" + f + ", ").orElse("") +
            optionalDegradationId().map(f -> "degradationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
