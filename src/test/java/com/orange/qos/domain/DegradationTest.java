package com.orange.qos.domain;

import static com.orange.qos.domain.DegradationTestSamples.*;
import static com.orange.qos.domain.SiteTestSamples.*;
import static com.orange.qos.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DegradationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Degradation.class);
        Degradation degradation1 = getDegradationSample1();
        Degradation degradation2 = new Degradation();
        assertThat(degradation1).isNotEqualTo(degradation2);

        degradation2.setId(degradation1.getId());
        assertThat(degradation1).isEqualTo(degradation2);

        degradation2 = getDegradationSample2();
        assertThat(degradation1).isNotEqualTo(degradation2);
    }

    @Test
    void utilisateurTest() {
        Degradation degradation = getDegradationRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        degradation.setUtilisateur(utilisateurBack);
        assertThat(degradation.getUtilisateur()).isEqualTo(utilisateurBack);

        degradation.utilisateur(null);
        assertThat(degradation.getUtilisateur()).isNull();
    }

    @Test
    void siteTest() {
        Degradation degradation = getDegradationRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        degradation.setSite(siteBack);
        assertThat(degradation.getSite()).isEqualTo(siteBack);

        degradation.site(null);
        assertThat(degradation.getSite()).isNull();
    }
}
