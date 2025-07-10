package com.orange.qos.service.mapper;

import static com.orange.qos.domain.DelaiInterventionAsserts.*;
import static com.orange.qos.domain.DelaiInterventionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DelaiInterventionMapperTest {

    private DelaiInterventionMapper delaiInterventionMapper;

    @BeforeEach
    void setUp() {
        delaiInterventionMapper = new DelaiInterventionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDelaiInterventionSample1();
        var actual = delaiInterventionMapper.toEntity(delaiInterventionMapper.toDto(expected));
        assertDelaiInterventionAllPropertiesEquals(expected, actual);
    }
}
