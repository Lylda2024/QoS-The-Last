package com.orange.qos.service.mapper;

import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.Notification;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "degradation", source = "degradation", qualifiedByName = "degradationId")
    NotificationDTO toDto(Notification s);

    @Named("degradationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DegradationDTO toDtoDegradationId(Degradation degradation);
}
