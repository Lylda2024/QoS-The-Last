package com.orange.qos.service.mapper;

import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.DelaiIntervention;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.dto.UtilisateurDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class DelaiInterventionMapper implements EntityMapper<DelaiInterventionDTO, DelaiIntervention> {

    /**
     * Mapping standard → copie tous les champs utiles, y compris dateLimite et statut.
     */
    @Named("toDto")
    @Mapping(target = "etatCouleur", ignore = true) // on le calcule après
    @Mapping(target = "degradation", source = "degradation", qualifiedByName = "degradationId")
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    // ⚠️ Ici, on s’assure que dateLimite et statut sont bien mappés automatiquement
    public abstract DelaiInterventionDTO toDto(DelaiIntervention entity);

    @Named("degradationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public abstract DegradationDTO toDtoDegradationId(Degradation degradation);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public abstract UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<DelaiInterventionDTO> toDto(List<DelaiIntervention> entityList);

    /**
     * Appelé automatiquement après `toDto()` pour calculer la couleur d’état en fonction du délai.
     */
    @AfterMapping
    protected void setEtatCouleur(@MappingTarget DelaiInterventionDTO dto, DelaiIntervention entity) {
        Instant dateLimite = entity.getDateLimite(); // mieux d’utiliser l’entity
        String statut = entity.getStatut() != null ? entity.getStatut().name() : null; // si c’est un enum

        if (dateLimite != null && statut != null) {
            Instant now = Instant.now();
            long daysBetween = ChronoUnit.DAYS.between(now, dateLimite);

            String couleur;

            switch (statut) {
                case "TERMINE":
                    couleur = "gris";
                    break;
                case "ANNULE":
                    couleur = "blanc";
                    break;
                case "EN_COURS":
                case "TRANSFERE":
                default:
                    if (daysBetween > 2) {
                        couleur = "vert";
                    } else if (daysBetween >= 0) {
                        couleur = "jaune";
                    } else {
                        couleur = "rouge";
                    }
                    break;
            }

            dto.setEtatCouleur(couleur);
        } else {
            // Si pas de date ou pas de statut → couleur par défaut
            dto.setEtatCouleur("blanc");
        }
    }

    /**
     * Appelle le mapping standard et applique automatiquement le calcul de la couleur d’état.
     */
    public DelaiInterventionDTO toDtoWithEtatCouleur(DelaiIntervention delai) {
        return toDto(delai); // déclenche aussi @AfterMapping -> setEtatCouleur()
    }
}
