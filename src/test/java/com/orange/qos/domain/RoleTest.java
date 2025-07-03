package com.orange.qos.domain;

import static com.orange.qos.domain.RoleTestSamples.*;
import static com.orange.qos.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = getRoleSample1();
        Role role2 = new Role();
        assertThat(role1).isNotEqualTo(role2);

        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);

        role2 = getRoleSample2();
        assertThat(role1).isNotEqualTo(role2);
    }

    @Test
    void utilisateursTest() {
        Role role = getRoleRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        role.addUtilisateurs(utilisateurBack);
        assertThat(role.getUtilisateurs()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getRoles()).containsOnly(role);

        role.removeUtilisateurs(utilisateurBack);
        assertThat(role.getUtilisateurs()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getRoles()).doesNotContain(role);

        role.utilisateurs(new HashSet<>(Set.of(utilisateurBack)));
        assertThat(role.getUtilisateurs()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getRoles()).containsOnly(role);

        role.setUtilisateurs(new HashSet<>());
        assertThat(role.getUtilisateurs()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getRoles()).doesNotContain(role);
    }
}
