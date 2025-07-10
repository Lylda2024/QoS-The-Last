package com.orange.qos.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.Degradation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DegradationDTO implements Serializable {

    private Long id;

    private String description;

    private Instant dateSignalement;

    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateSignalement() {
        return dateSignalement;
    }

    public void setDateSignalement(Instant dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DegradationDTO)) {
            return false;
        }

        DegradationDTO degradationDTO = (DegradationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, degradationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DegradationDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", dateSignalement='" + getDateSignalement() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
