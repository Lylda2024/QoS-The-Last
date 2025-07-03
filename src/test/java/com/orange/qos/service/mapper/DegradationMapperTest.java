package com.orange.qos.service.mapper;

import static com.orange.qos.domain.DegradationAsserts.*;
import static com.orange.qos.domain.DegradationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DegradationMapperTest {

    private DegradationMapper degradationMapper;

    @BeforeEach
    void setUp() {
        degradationMapper = new DegradationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDegradationSample1();
        var actual = degradationMapper.toEntity(degradationMapper.toDto(expected));
        assertDegradationAllPropertiesEquals(expected, actual);
    }
}
