package com.orange.qos.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.orange.qos.domain.TypeUtilisateur} entity. This class is used
 * in {@link com.orange.qos.web.rest.TypeUtilisateurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /type-utilisateurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeUtilisateurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter description;

    private IntegerFilter niveau;

    private StringFilter permissions;

    private Boolean distinct;

    public TypeUtilisateurCriteria() {}

    public TypeUtilisateurCriteria(TypeUtilisateurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.niveau = other.optionalNiveau().map(IntegerFilter::copy).orElse(null);
        this.permissions = other.optionalPermissions().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TypeUtilisateurCriteria copy() {
        return new TypeUtilisateurCriteria(this);
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

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
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

    public IntegerFilter getNiveau() {
        return niveau;
    }

    public Optional<IntegerFilter> optionalNiveau() {
        return Optional.ofNullable(niveau);
    }

    public IntegerFilter niveau() {
        if (niveau == null) {
            setNiveau(new IntegerFilter());
        }
        return niveau;
    }

    public void setNiveau(IntegerFilter niveau) {
        this.niveau = niveau;
    }

    public StringFilter getPermissions() {
        return permissions;
    }

    public Optional<StringFilter> optionalPermissions() {
        return Optional.ofNullable(permissions);
    }

    public StringFilter permissions() {
        if (permissions == null) {
            setPermissions(new StringFilter());
        }
        return permissions;
    }

    public void setPermissions(StringFilter permissions) {
        this.permissions = permissions;
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
        final TypeUtilisateurCriteria that = (TypeUtilisateurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(niveau, that.niveau) &&
            Objects.equals(permissions, that.permissions) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, niveau, permissions, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeUtilisateurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalNiveau().map(f -> "niveau=" + f + ", ").orElse("") +
            optionalPermissions().map(f -> "permissions=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
