package com.orange.qos.domain;

import static com.orange.qos.domain.DegradationTestSamples.*;
import static com.orange.qos.domain.DelaiInterventionTestSamples.*;
import static com.orange.qos.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DelaiInterventionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DelaiIntervention.class);
        DelaiIntervention delaiIntervention1 = getDelaiInterventionSample1();
        DelaiIntervention delaiIntervention2 = new DelaiIntervention();
        assertThat(delaiIntervention1).isNotEqualTo(delaiIntervention2);

        delaiIntervention2.setId(delaiIntervention1.getId());
        assertThat(delaiIntervention1).isEqualTo(delaiIntervention2);

        delaiIntervention2 = getDelaiInterventionSample2();
        assertThat(delaiIntervention1).isNotEqualTo(delaiIntervention2);
    }

    @Test
    void degradationTest() {
        DelaiIntervention delaiIntervention = getDelaiInterventionRandomSampleGenerator();
        Degradation degradationBack = getDegradationRandomSampleGenerator();

        delaiIntervention.setDegradation(degradationBack);
        assertThat(delaiIntervention.getDegradation()).isEqualTo(degradationBack);

        delaiIntervention.degradation(null);
        assertThat(delaiIntervention.getDegradation()).isNull();
    }

    @Test
    void utilisateurTest() {
        DelaiIntervention delaiIntervention = getDelaiInterventionRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        delaiIntervention.setUtilisateur(utilisateurBack);
        assertThat(delaiIntervention.getUtilisateur()).isEqualTo(utilisateurBack);

        delaiIntervention.utilisateur(null);
        assertThat(delaiIntervention.getUtilisateur()).isNull();
    }
}
