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

    @Named("toDto")
    @Mapping(target = "etatCouleur", ignore = true)
    @Mapping(target = "degradation", source = "degradation", qualifiedByName = "degradationId")
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
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
     * Appelé automatiquement après `toDto` pour calculer et fixer la couleur d’état.
     */
    @AfterMapping
    protected void setEtatCouleur(@MappingTarget DelaiInterventionDTO dto, DelaiIntervention entity) {
        if (dto.getDateLimite() != null && dto.getStatut() != null) {
            Instant now = Instant.now();
            long daysBetween = ChronoUnit.DAYS.between(now, dto.getDateLimite());

            String couleur;
            switch (dto.getStatut()) {
                case TERMINE:
                    couleur = "gris";
                    break;
                case ANNULE:
                    couleur = "blanc";
                    break;
                case EN_COURS:
                case TRANSFERE:
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
            dto.setEtatCouleur("blanc");
        }
    }

    /**
     * Appelle le mapping standard et applique le calcul de la couleur d’état.
     */
    public DelaiInterventionDTO toDtoWithEtatCouleur(DelaiIntervention delai) {
        return toDto(delai); // déclenche aussi @AfterMapping -> setEtatCouleur
    }
}
