package io.aurigraph.healthcare.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComorbidityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComorbidityDTO.class);
        ComorbidityDTO comorbidityDTO1 = new ComorbidityDTO();
        comorbidityDTO1.setId(1L);
        ComorbidityDTO comorbidityDTO2 = new ComorbidityDTO();
        assertThat(comorbidityDTO1).isNotEqualTo(comorbidityDTO2);
        comorbidityDTO2.setId(comorbidityDTO1.getId());
        assertThat(comorbidityDTO1).isEqualTo(comorbidityDTO2);
        comorbidityDTO2.setId(2L);
        assertThat(comorbidityDTO1).isNotEqualTo(comorbidityDTO2);
        comorbidityDTO1.setId(null);
        assertThat(comorbidityDTO1).isNotEqualTo(comorbidityDTO2);
    }
}
