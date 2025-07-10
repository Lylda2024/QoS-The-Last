package com.orange.qos.service.mapper;

import com.orange.qos.domain.Degradation;
import com.orange.qos.service.dto.DegradationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Degradation} and its DTO {@link DegradationDTO}.
 */
@Mapper(componentModel = "spring")
public interface DegradationMapper extends EntityMapper<DegradationDTO, Degradation> {}
