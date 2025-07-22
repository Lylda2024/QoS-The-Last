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

    @NotNull(message = "Le nom du site ne peut pas être null")
    @Size(min = 2, max = 100, message = "Le nom du site doit contenir entre 2 et 100 caractères")
    private String nomSite;

    @Size(max = 50, message = "Le code OCI ne peut pas dépasser 50 caractères")
    private String codeOCI;

    @NotNull(message = "La longitude ne peut pas être null")
    @DecimalMin(value = "-180.0", message = "La longitude doit être entre -180 et 180")
    @DecimalMax(value = "180.0", message = "La longitude doit être entre -180 et 180")
    private Float longitude;

    @NotNull(message = "La latitude ne peut pas être null")
    @DecimalMin(value = "-90.0", message = "La latitude doit être entre -90 et 90")
    @DecimalMax(value = "90.0", message = "La latitude doit être entre -90 et 90")
    private Float latitude;

    @Size(max = 50, message = "Le statut ne peut pas dépasser 50 caractères")
    private String statut;

    @Size(max = 50, message = "La technologie ne peut pas dépasser 50 caractères")
    private String technologie;

    @Size(max = 100, message = "La ville ne peut pas dépasser 100 caractères")
    private String ville;

    @Size(max = 50, message = "Le type de site ne peut pas dépasser 50 caractères")
    private String typeSite;

    @NotNull(message = "Le statut en service ne peut pas être null")
    private Boolean enService;

    // Constructeurs
    public SiteDTO() {
        // Constructeur par défaut
    }

    public SiteDTO(
        Long id,
        String nomSite,
        String codeOCI,
        Float longitude,
        Float latitude,
        String statut,
        String technologie,
        String ville,
        String typeSite,
        Boolean enService
    ) {
        this.id = id;
        this.nomSite = nomSite;
        this.codeOCI = codeOCI;
        this.longitude = longitude;
        this.latitude = latitude;
        this.statut = statut;
        this.technologie = technologie;
        this.ville = ville;
        this.typeSite = typeSite;
        this.enService = enService;
    }

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

    // Méthode utilitaire pour validation
    public boolean isCoordinatesValid() {
        return longitude != null && latitude != null && longitude >= -180 && longitude <= 180 && latitude >= -90 && latitude <= 90;
    }
}
