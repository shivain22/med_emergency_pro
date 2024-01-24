package io.aurigraph.healthcare.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientComorbidityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientComorbidityDTO.class);
        PatientComorbidityDTO patientComorbidityDTO1 = new PatientComorbidityDTO();
        patientComorbidityDTO1.setId(1L);
        PatientComorbidityDTO patientComorbidityDTO2 = new PatientComorbidityDTO();
        assertThat(patientComorbidityDTO1).isNotEqualTo(patientComorbidityDTO2);
        patientComorbidityDTO2.setId(patientComorbidityDTO1.getId());
        assertThat(patientComorbidityDTO1).isEqualTo(patientComorbidityDTO2);
        patientComorbidityDTO2.setId(2L);
        assertThat(patientComorbidityDTO1).isNotEqualTo(patientComorbidityDTO2);
        patientComorbidityDTO1.setId(null);
        assertThat(patientComorbidityDTO1).isNotEqualTo(patientComorbidityDTO2);
    }
}
