package com.orange.qos.domain;

import static com.orange.qos.domain.RoleTestSamples.*;
import static com.orange.qos.domain.TypeUtilisateurTestSamples.*;
import static com.orange.qos.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void typeUtilisateurTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        TypeUtilisateur typeUtilisateurBack = getTypeUtilisateurRandomSampleGenerator();

        utilisateur.setTypeUtilisateur(typeUtilisateurBack);
        assertThat(utilisateur.getTypeUtilisateur()).isEqualTo(typeUtilisateurBack);

        utilisateur.typeUtilisateur(null);
        assertThat(utilisateur.getTypeUtilisateur()).isNull();
    }

    @Test
    void rolesTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        utilisateur.addRoles(roleBack);
        assertThat(utilisateur.getRoles()).containsOnly(roleBack);

        utilisateur.removeRoles(roleBack);
        assertThat(utilisateur.getRoles()).doesNotContain(roleBack);

        utilisateur.roles(new HashSet<>(Set.of(roleBack)));
        assertThat(utilisateur.getRoles()).containsOnly(roleBack);

        utilisateur.setRoles(new HashSet<>());
        assertThat(utilisateur.getRoles()).doesNotContain(roleBack);
    }
}
