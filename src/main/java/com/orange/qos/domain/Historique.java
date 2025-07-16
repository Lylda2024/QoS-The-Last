package com.orange.qos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Historique.
 */
@Entity
@Table(name = "historique")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Historique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "utilisateur")
    private String utilisateur;

    @Column(name = "section")
    private String section;

    @NotNull
    @Column(name = "horodatage", nullable = false)
    private Instant horodatage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "utilisateur", "site" }, allowSetters = true)
    private Degradation degradation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Historique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUtilisateur() {
        return this.utilisateur;
    }

    public Historique utilisateur(String utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getSection() {
        return this.section;
    }

    public Historique section(String section) {
        this.setSection(section);
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Instant getHorodatage() {
        return this.horodatage;
    }

    public Historique horodatage(Instant horodatage) {
        this.setHorodatage(horodatage);
        return this;
    }

    public void setHorodatage(Instant horodatage) {
        this.horodatage = horodatage;
    }

    public Degradation getDegradation() {
        return this.degradation;
    }

    public void setDegradation(Degradation degradation) {
        this.degradation = degradation;
    }

    public Historique degradation(Degradation degradation) {
        this.setDegradation(degradation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Historique)) {
            return false;
        }
        return getId() != null && getId().equals(((Historique) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historique{" +
            "id=" + getId() +
            ", utilisateur='" + getUtilisateur() + "'" +
            ", section='" + getSection() + "'" +
            ", horodatage='" + getHorodatage() + "'" +
            "}";
    }
}
