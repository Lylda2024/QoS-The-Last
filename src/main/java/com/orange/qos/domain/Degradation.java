package com.orange.qos.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Degradation.
 */
@Entity
@Table(name = "degradation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Degradation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "date_signalement")
    private Instant dateSignalement;

    @Column(name = "statut")
    private String statut;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Degradation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Degradation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateSignalement() {
        return this.dateSignalement;
    }

    public Degradation dateSignalement(Instant dateSignalement) {
        this.setDateSignalement(dateSignalement);
        return this;
    }

    public void setDateSignalement(Instant dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public String getStatut() {
        return this.statut;
    }

    public Degradation statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Degradation)) {
            return false;
        }
        return getId() != null && getId().equals(((Degradation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Degradation{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", dateSignalement='" + getDateSignalement() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
