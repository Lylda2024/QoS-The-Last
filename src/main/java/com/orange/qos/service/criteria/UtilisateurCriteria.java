package com.orange.qos.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.orange.qos.domain.Utilisateur} entity. This class is used
 * in {@link com.orange.qos.web.rest.UtilisateurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /utilisateurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter email;

    private StringFilter motDePasse;

    private LongFilter typeUtilisateurId;

    private LongFilter rolesId;

    private Boolean distinct;

    public UtilisateurCriteria() {}

    public UtilisateurCriteria(UtilisateurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.motDePasse = other.optionalMotDePasse().map(StringFilter::copy).orElse(null);
        this.typeUtilisateurId = other.optionalTypeUtilisateurId().map(LongFilter::copy).orElse(null);
        this.rolesId = other.optionalRolesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UtilisateurCriteria copy() {
        return new UtilisateurCriteria(this);
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

    public StringFilter getPrenom() {
        return prenom;
    }

    public Optional<StringFilter> optionalPrenom() {
        return Optional.ofNullable(prenom);
    }

    public StringFilter prenom() {
        if (prenom == null) {
            setPrenom(new StringFilter());
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getMotDePasse() {
        return motDePasse;
    }

    public Optional<StringFilter> optionalMotDePasse() {
        return Optional.ofNullable(motDePasse);
    }

    public StringFilter motDePasse() {
        if (motDePasse == null) {
            setMotDePasse(new StringFilter());
        }
        return motDePasse;
    }

    public void setMotDePasse(StringFilter motDePasse) {
        this.motDePasse = motDePasse;
    }

    public LongFilter getTypeUtilisateurId() {
        return typeUtilisateurId;
    }

    public Optional<LongFilter> optionalTypeUtilisateurId() {
        return Optional.ofNullable(typeUtilisateurId);
    }

    public LongFilter typeUtilisateurId() {
        if (typeUtilisateurId == null) {
            setTypeUtilisateurId(new LongFilter());
        }
        return typeUtilisateurId;
    }

    public void setTypeUtilisateurId(LongFilter typeUtilisateurId) {
        this.typeUtilisateurId = typeUtilisateurId;
    }

    public LongFilter getRolesId() {
        return rolesId;
    }

    public Optional<LongFilter> optionalRolesId() {
        return Optional.ofNullable(rolesId);
    }

    public LongFilter rolesId() {
        if (rolesId == null) {
            setRolesId(new LongFilter());
        }
        return rolesId;
    }

    public void setRolesId(LongFilter rolesId) {
        this.rolesId = rolesId;
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
        final UtilisateurCriteria that = (UtilisateurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(email, that.email) &&
            Objects.equals(motDePasse, that.motDePasse) &&
            Objects.equals(typeUtilisateurId, that.typeUtilisateurId) &&
            Objects.equals(rolesId, that.rolesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, email, motDePasse, typeUtilisateurId, rolesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalMotDePasse().map(f -> "motDePasse=" + f + ", ").orElse("") +
            optionalTypeUtilisateurId().map(f -> "typeUtilisateurId=" + f + ", ").orElse("") +
            optionalRolesId().map(f -> "rolesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
