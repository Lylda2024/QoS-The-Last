package com.orange.qos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Degradation.
 */
@Entity
@Table(name = "degradation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Degradation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "localite", nullable = false)
    private String localite;

    @NotNull
    @Column(name = "contact_temoin", nullable = false)
    private String contactTemoin;

    @NotNull
    @Column(name = "type_anomalie", nullable = false)
    private String typeAnomalie;

    @NotNull
    @Column(name = "priorite", nullable = false)
    private String priorite;

    @NotNull
    @Column(name = "problem", nullable = false)
    private String problem;

    @NotNull
    @Column(name = "porteur", nullable = false)
    private String porteur;

    @Column(name = "actions_effectuees")
    private String actionsEffectuees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "typeUtilisateur", "roles" }, allowSetters = true)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    private Site site;

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

    public String getNumero() {
        return this.numero;
    }

    public Degradation numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLocalite() {
        return this.localite;
    }

    public Degradation localite(String localite) {
        this.setLocalite(localite);
        return this;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getContactTemoin() {
        return this.contactTemoin;
    }

    public Degradation contactTemoin(String contactTemoin) {
        this.setContactTemoin(contactTemoin);
        return this;
    }

    public void setContactTemoin(String contactTemoin) {
        this.contactTemoin = contactTemoin;
    }

    public String getTypeAnomalie() {
        return this.typeAnomalie;
    }

    public Degradation typeAnomalie(String typeAnomalie) {
        this.setTypeAnomalie(typeAnomalie);
        return this;
    }

    public void setTypeAnomalie(String typeAnomalie) {
        this.typeAnomalie = typeAnomalie;
    }

    public String getPriorite() {
        return this.priorite;
    }

    public Degradation priorite(String priorite) {
        this.setPriorite(priorite);
        return this;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getProblem() {
        return this.problem;
    }

    public Degradation problem(String problem) {
        this.setProblem(problem);
        return this;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getPorteur() {
        return this.porteur;
    }

    public Degradation porteur(String porteur) {
        this.setPorteur(porteur);
        return this;
    }

    public void setPorteur(String porteur) {
        this.porteur = porteur;
    }

    public String getActionsEffectuees() {
        return this.actionsEffectuees;
    }

    public Degradation actionsEffectuees(String actionsEffectuees) {
        this.setActionsEffectuees(actionsEffectuees);
        return this;
    }

    public void setActionsEffectuees(String actionsEffectuees) {
        this.actionsEffectuees = actionsEffectuees;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Degradation utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Degradation site(Site site) {
        this.setSite(site);
        return this;
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
            ", numero='" + getNumero() + "'" +
            ", localite='" + getLocalite() + "'" +
            ", contactTemoin='" + getContactTemoin() + "'" +
            ", typeAnomalie='" + getTypeAnomalie() + "'" +
            ", priorite='" + getPriorite() + "'" +
            ", problem='" + getProblem() + "'" +
            ", porteur='" + getPorteur() + "'" +
            ", actionsEffectuees='" + getActionsEffectuees() + "'" +
            "}";
    }
}
