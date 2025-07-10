package com.orange.qos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DelaiInterventionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DelaiInterventionDTO.class);
        DelaiInterventionDTO delaiInterventionDTO1 = new DelaiInterventionDTO();
        delaiInterventionDTO1.setId(1L);
        DelaiInterventionDTO delaiInterventionDTO2 = new DelaiInterventionDTO();
        assertThat(delaiInterventionDTO1).isNotEqualTo(delaiInterventionDTO2);
        delaiInterventionDTO2.setId(delaiInterventionDTO1.getId());
        assertThat(delaiInterventionDTO1).isEqualTo(delaiInterventionDTO2);
        delaiInterventionDTO2.setId(2L);
        assertThat(delaiInterventionDTO1).isNotEqualTo(delaiInterventionDTO2);
        delaiInterventionDTO1.setId(null);
        assertThat(delaiInterventionDTO1).isNotEqualTo(delaiInterventionDTO2);
    }
}
