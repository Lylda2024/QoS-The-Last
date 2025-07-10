package com.orange.qos.domain;

import static com.orange.qos.domain.DegradationTestSamples.*;
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
}
