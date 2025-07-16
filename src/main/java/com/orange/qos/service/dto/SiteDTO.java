package com.orange.qos.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.Site} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomSite;

    private String codeOCI;

    @NotNull
    private Float longitude;

    @NotNull
    private Float latitude;

    private String statut;

    private String technologie;

    private String ville; // ✅ Ajouté
    private String typeSite; // ✅ Ajouté

    private Boolean enService;

    // Getters / Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSite() {
        return nomSite;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public String getCodeOCI() {
        return codeOCI;
    }

    public void setCodeOCI(String codeOCI) {
        this.codeOCI = codeOCI;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTechnologie() {
        return technologie;
    }

    public void setTechnologie(String technologie) {
        this.technologie = technologie;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTypeSite() {
        return typeSite;
    }

    public void setTypeSite(String typeSite) {
        this.typeSite = typeSite;
    }

    public Boolean getEnService() {
        return enService;
    }

    public void setEnService(Boolean enService) {
        this.enService = enService;
    }

    // equals, hashCode, toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteDTO)) return false;
        SiteDTO siteDTO = (SiteDTO) o;
        return id != null && Objects.equals(id, siteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "SiteDTO{" +
            "id=" +
            id +
            ", nomSite='" +
            nomSite +
            '\'' +
            ", codeOCI='" +
            codeOCI +
            '\'' +
            ", longitude=" +
            longitude +
            ", latitude=" +
            latitude +
            ", statut='" +
            statut +
            '\'' +
            ", technologie='" +
            technologie +
            '\'' +
            ", ville='" +
            ville +
            '\'' +
            ", typeSite='" +
            typeSite +
            '\'' +
            ", enService=" +
            enService +
            '}'
        );
    }
}
