package com.orange.qos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Site.
 */
@Entity
@Table(name = "site")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_site", nullable = false)
    private String nomSite;

    @Column(name = "code_oci")
    private String codeOCI;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "statut")
    private String statut;

    @Column(name = "technologie")
    private String technologie;

    @Column(name = "en_service")
    private Boolean enService;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Site id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSite() {
        return this.nomSite;
    }

    public Site nomSite(String nomSite) {
        this.setNomSite(nomSite);
        return this;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public String getCodeOCI() {
        return this.codeOCI;
    }

    public Site codeOCI(String codeOCI) {
        this.setCodeOCI(codeOCI);
        return this;
    }

    public void setCodeOCI(String codeOCI) {
        this.codeOCI = codeOCI;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public Site longitude(Float longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Site latitude(Float latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getStatut() {
        return this.statut;
    }

    public Site statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTechnologie() {
        return this.technologie;
    }

    public Site technologie(String technologie) {
        this.setTechnologie(technologie);
        return this;
    }

    public void setTechnologie(String technologie) {
        this.technologie = technologie;
    }

    public Boolean getEnService() {
        return this.enService;
    }

    public Site enService(Boolean enService) {
        this.setEnService(enService);
        return this;
    }

    public void setEnService(Boolean enService) {
        this.enService = enService;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return getId() != null && getId().equals(((Site) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", nomSite='" + getNomSite() + "'" +
            ", codeOCI='" + getCodeOCI() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", statut='" + getStatut() + "'" +
            ", technologie='" + getTechnologie() + "'" +
            ", enService='" + getEnService() + "'" +
            "}";
    }
}
