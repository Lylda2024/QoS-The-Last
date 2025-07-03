package com.orange.qos.domain;

import static com.orange.qos.domain.DegradationTestSamples.*;
import static com.orange.qos.domain.HistoriqueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historique.class);
        Historique historique1 = getHistoriqueSample1();
        Historique historique2 = new Historique();
        assertThat(historique1).isNotEqualTo(historique2);

        historique2.setId(historique1.getId());
        assertThat(historique1).isEqualTo(historique2);

        historique2 = getHistoriqueSample2();
        assertThat(historique1).isNotEqualTo(historique2);
    }

    @Test
    void degradationTest() {
        Historique historique = getHistoriqueRandomSampleGenerator();
        Degradation degradationBack = getDegradationRandomSampleGenerator();

        historique.setDegradation(degradationBack);
        assertThat(historique.getDegradation()).isEqualTo(degradationBack);

        historique.degradation(null);
        assertThat(historique.getDegradation()).isNull();
    }
}
