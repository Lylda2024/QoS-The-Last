package com.orange.qos.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.Degradation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DegradationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String localite;

    @NotNull
    private String contactTemoin;

    @NotNull
    private String typeAnomalie;

    @NotNull
    private String priorite;

    @NotNull
    private String porteur;

    private String porteur2;

    private String actionsEffectuees;

    private String statut;

    private String commentaire;

    private String ticketOceane;

    private String nextStep;

    @NotNull
    private Instant dateDetection;

    private Instant dateLimite;

    private UtilisateurDTO utilisateur;
    private SiteDTO site;

    private List<DelaiInterventionDTO> delais;

    // ============ Getters / Setters ============

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

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    public List<DelaiInterventionDTO> getDelais() {
        return delais;
    }

    public void setDelais(List<DelaiInterventionDTO> delais) {
        this.delais = delais;
    }

    // ============ equals / hashCode ============

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DegradationDTO)) return false;
        DegradationDTO that = (DegradationDTO) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ============ toString ============

    @Override
    public String toString() {
        return (
            "DegradationDTO{" +
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
            ", utilisateur=" +
            utilisateur +
            ", site=" +
            site +
            ", delais=" +
            (delais != null ? delais.size() + " éléments" : "null") +
            '}'
        );
    }
}
