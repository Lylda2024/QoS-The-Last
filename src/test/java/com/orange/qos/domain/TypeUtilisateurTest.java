package com.orange.qos.domain;

import static com.orange.qos.domain.TypeUtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeUtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeUtilisateur.class);
        TypeUtilisateur typeUtilisateur1 = getTypeUtilisateurSample1();
        TypeUtilisateur typeUtilisateur2 = new TypeUtilisateur();
        assertThat(typeUtilisateur1).isNotEqualTo(typeUtilisateur2);

        typeUtilisateur2.setId(typeUtilisateur1.getId());
        assertThat(typeUtilisateur1).isEqualTo(typeUtilisateur2);

        typeUtilisateur2 = getTypeUtilisateurSample2();
        assertThat(typeUtilisateur1).isNotEqualTo(typeUtilisateur2);
    }
}
