package com.orange.qos.service.mapper;

import static com.orange.qos.domain.TypeUtilisateurAsserts.*;
import static com.orange.qos.domain.TypeUtilisateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeUtilisateurMapperTest {

    private TypeUtilisateurMapper typeUtilisateurMapper;

    @BeforeEach
    void setUp() {
        typeUtilisateurMapper = new TypeUtilisateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeUtilisateurSample1();
        var actual = typeUtilisateurMapper.toEntity(typeUtilisateurMapper.toDto(expected));
        assertTypeUtilisateurAllPropertiesEquals(expected, actual);
    }
}
