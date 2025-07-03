package com.orange.qos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeUtilisateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeUtilisateurDTO.class);
        TypeUtilisateurDTO typeUtilisateurDTO1 = new TypeUtilisateurDTO();
        typeUtilisateurDTO1.setId(1L);
        TypeUtilisateurDTO typeUtilisateurDTO2 = new TypeUtilisateurDTO();
        assertThat(typeUtilisateurDTO1).isNotEqualTo(typeUtilisateurDTO2);
        typeUtilisateurDTO2.setId(typeUtilisateurDTO1.getId());
        assertThat(typeUtilisateurDTO1).isEqualTo(typeUtilisateurDTO2);
        typeUtilisateurDTO2.setId(2L);
        assertThat(typeUtilisateurDTO1).isNotEqualTo(typeUtilisateurDTO2);
        typeUtilisateurDTO1.setId(null);
        assertThat(typeUtilisateurDTO1).isNotEqualTo(typeUtilisateurDTO2);
    }
}
