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
    @Mapping(target = "etatCouleur", ignore = true) // Calculé après mapping
    @Mapping(target = "degradation", source = "degradation", qualifiedByName = "degradationId")
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    public abstract DelaiInterventionDTO toDto(DelaiIntervention entity);

    @IterableMapping(qualifiedByName = "toDto", elementTargetType = DelaiInterventionDTO.class)
    public abstract List<DelaiInterventionDTO> toDto(List<DelaiIntervention> entityList);

    // Pour la conversion inverse, map chaque DTO vers entité sans IterableMapping (optionnel)
    public abstract List<DelaiIntervention> toEntity(List<DelaiInterventionDTO> dtoList);

    @Mapping(target = "degradation", ignore = true) // à gérer dans le service si besoin
    @Mapping(target = "utilisateur", ignore = true) // idem
    public abstract DelaiIntervention toEntity(DelaiInterventionDTO dto);

    @Named("degradationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public abstract DegradationDTO toDtoDegradationId(Degradation degradation);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public abstract UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @AfterMapping
    protected void setEtatCouleur(@MappingTarget DelaiInterventionDTO dto, DelaiIntervention entity) {
        Instant dateLimite = entity.getDateLimite();
        String statut = entity.getStatut() != null ? entity.getStatut().name() : null;

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
            dto.setEtatCouleur("blanc");
        }
    }

    public DelaiInterventionDTO toDtoWithEtatCouleur(DelaiIntervention delai) {
        return toDto(delai); // déclenche aussi @AfterMapping -> setEtatCouleur()
    }
}
