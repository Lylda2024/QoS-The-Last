package com.orange.qos.service.mapper;

import static com.orange.qos.domain.HistoriqueAsserts.*;
import static com.orange.qos.domain.HistoriqueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueMapperTest {

    private HistoriqueMapper historiqueMapper;

    @BeforeEach
    void setUp() {
        historiqueMapper = new HistoriqueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoriqueSample1();
        var actual = historiqueMapper.toEntity(historiqueMapper.toDto(expected));
        assertHistoriqueAllPropertiesEquals(expected, actual);
    }
}
