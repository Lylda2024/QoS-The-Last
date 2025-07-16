package com.orange.qos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A TypeUtilisateur.
 */
@Entity
@Table(name = "type_utilisateur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeUtilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "niveau")
    private Integer niveau;

    @Column(name = "permissions")
    private String permissions;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeUtilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public TypeUtilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public TypeUtilisateur description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNiveau() {
        return this.niveau;
    }

    public TypeUtilisateur niveau(Integer niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public TypeUtilisateur permissions(String permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeUtilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeUtilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeUtilisateur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", niveau=" + getNiveau() +
            ", permissions='" + getPermissions() + "'" +
            "}";
    }
}
