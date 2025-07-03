package com.orange.qos.service.mapper;

import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.Historique;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.HistoriqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Historique} and its DTO {@link HistoriqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueMapper extends EntityMapper<HistoriqueDTO, Historique> {
    @Mapping(target = "degradation", source = "degradation", qualifiedByName = "degradationId")
    HistoriqueDTO toDto(Historique s);

    @Named("degradationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DegradationDTO toDtoDegradationId(Degradation degradation);
}
