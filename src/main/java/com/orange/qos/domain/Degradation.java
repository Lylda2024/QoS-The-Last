package com.orange.qos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "degradation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Degradation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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
    @Column(name = "porteur", nullable = false)
    private String porteur;

    @Column(name = "porteur2")
    private String porteur2;

    @Column(name = "actions_effectuees")
    private String actionsEffectuees;

    @Column(name = "statut")
    private String statut;

    @Column(name = "commentaire", length = 1000)
    private String commentaire;

    @Column(name = "ticket_oceane")
    private String ticketOceane;

    @Column(name = "next_step")
    private String nextStep;

    @NotNull
    @Column(name = "date_detection", nullable = false)
    private Instant dateDetection;

    @Column(name = "date_limite")
    private Instant dateLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "typeUtilisateur", "roles" }, allowSetters = true)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    private Site site;

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getContactTemoin() {
        return contactTemoin;
    }

    public void setContactTemoin(String contactTemoin) {
        this.contactTemoin = contactTemoin;
    }

    public String getTypeAnomalie() {
        return typeAnomalie;
    }

    public void setTypeAnomalie(String typeAnomalie) {
        this.typeAnomalie = typeAnomalie;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getPorteur() {
        return porteur;
    }

    public void setPorteur(String porteur) {
        this.porteur = porteur;
    }

    public String getPorteur2() {
        return porteur2;
    }

    public void setPorteur2(String porteur2) {
        this.porteur2 = porteur2;
    }

    public String getActionsEffectuees() {
        return actionsEffectuees;
    }

    public void setActionsEffectuees(String actionsEffectuees) {
        this.actionsEffectuees = actionsEffectuees;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getTicketOceane() {
        return ticketOceane;
    }

    public void setTicketOceane(String ticketOceane) {
        this.ticketOceane = ticketOceane;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public Instant getDateDetection() {
        return dateDetection;
    }

    public void setDateDetection(Instant dateDetection) {
        this.dateDetection = dateDetection;
    }

    public Instant getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Instant dateLimite) {
        this.dateLimite = dateLimite;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    // ---------- equals & hashCode (safe for JPA) ----------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Degradation)) return false;
        return id != null && id.equals(((Degradation) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // ---------- toString ----------

    @Override
    public String toString() {
        return (
            "Degradation{" +
            "id=" +
            id +
            ", localite='" +
            localite +
            '\'' +
            ", contactTemoin='" +
            contactTemoin +
            '\'' +
            ", typeAnomalie='" +
            typeAnomalie +
            '\'' +
            ", priorite='" +
            priorite +
            '\'' +
            ", porteur='" +
            porteur +
            '\'' +
            ", porteur2='" +
            porteur2 +
            '\'' +
            ", actionsEffectuees='" +
            actionsEffectuees +
            '\'' +
            ", statut='" +
            statut +
            '\'' +
            ", commentaire='" +
            commentaire +
            '\'' +
            ", ticketOceane='" +
            ticketOceane +
            '\'' +
            ", nextStep='" +
            nextStep +
            '\'' +
            ", dateDetection=" +
            dateDetection +
            ", dateLimite=" +
            dateLimite +
            '}'
        );
    }

    public Degradation id(Long id) {
        this.id = id;
        return this;
    }

    public Degradation localite(String localite) {
        this.localite = localite;
        return this;
    }

    public Degradation contactTemoin(String contactTemoin) {
        this.contactTemoin = contactTemoin;
        return this;
    }

    public Degradation typeAnomalie(String typeAnomalie) {
        this.typeAnomalie = typeAnomalie;
        return this;
    }

    public Degradation priorite(String priorite) {
        this.priorite = priorite;
        return this;
    }

    public Degradation porteur(String porteur) {
        this.porteur = porteur;
        return this;
    }

    public Degradation porteur2(String porteur2) {
        this.porteur2 = porteur2;
        return this;
    }

    public Degradation actionsEffectuees(String actionsEffectuees) {
        this.actionsEffectuees = actionsEffectuees;
        return this;
    }

    public Degradation statut(String statut) {
        this.statut = statut;
        return this;
    }

    public Degradation commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public Degradation ticketOceane(String ticketOceane) {
        this.ticketOceane = ticketOceane;
        return this;
    }

    public Degradation nextStep(String nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    public Degradation dateDetection(Instant dateDetection) {
        this.dateDetection = dateDetection;
        return this;
    }

    public Degradation dateLimite(Instant dateLimite) {
        this.dateLimite = dateLimite;
        return this;
    }

    public Degradation utilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        return this;
    }

    public Degradation site(Site site) {
        this.site = site;
        return this;
    }
}
