package com.orange.qos.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.Historique} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueDTO implements Serializable {

    private Long id;

    private String utilisateur;

    private String section;

    @NotNull
    private Instant horodatage;

    private DegradationDTO degradation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Instant getHorodatage() {
        return horodatage;
    }

    public void setHorodatage(Instant horodatage) {
        this.horodatage = horodatage;
    }

    public DegradationDTO getDegradation() {
        return degradation;
    }

    public void setDegradation(DegradationDTO degradation) {
        this.degradation = degradation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueDTO)) {
            return false;
        }

        HistoriqueDTO historiqueDTO = (HistoriqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueDTO{" +
            "id=" + getId() +
            ", utilisateur='" + getUtilisateur() + "'" +
            ", section='" + getSection() + "'" +
            ", horodatage='" + getHorodatage() + "'" +
            ", degradation=" + getDegradation() +
            "}";
    }
}
