package com.orange.qos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DegradationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DegradationDTO.class);
        DegradationDTO degradationDTO1 = new DegradationDTO();
        degradationDTO1.setId(1L);
        DegradationDTO degradationDTO2 = new DegradationDTO();
        assertThat(degradationDTO1).isNotEqualTo(degradationDTO2);
        degradationDTO2.setId(degradationDTO1.getId());
        assertThat(degradationDTO1).isEqualTo(degradationDTO2);
        degradationDTO2.setId(2L);
        assertThat(degradationDTO1).isNotEqualTo(degradationDTO2);
        degradationDTO1.setId(null);
        assertThat(degradationDTO1).isNotEqualTo(degradationDTO2);
    }
}
