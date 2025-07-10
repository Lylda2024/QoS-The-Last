package com.orange.qos.service.mapper;

import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.DelaiIntervention;
import com.orange.qos.domain.User;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DelaiIntervention} and its DTO {@link DelaiInterventionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DelaiInterventionMapper extends EntityMapper<DelaiInterventionDTO, DelaiIntervention> {
    @Mapping(target = "acteur", source = "acteur", qualifiedByName = "userLogin")
    @Mapping(target = "degradation", source = "degradation", qualifiedByName = "degradationId")
    DelaiInterventionDTO toDto(DelaiIntervention s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("degradationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DegradationDTO toDtoDegradationId(Degradation degradation);
}
