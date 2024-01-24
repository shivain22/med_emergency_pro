package io.aurigraph.healthcare.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientVitalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientVitalDTO.class);
        PatientVitalDTO patientVitalDTO1 = new PatientVitalDTO();
        patientVitalDTO1.setId(1L);
        PatientVitalDTO patientVitalDTO2 = new PatientVitalDTO();
        assertThat(patientVitalDTO1).isNotEqualTo(patientVitalDTO2);
        patientVitalDTO2.setId(patientVitalDTO1.getId());
        assertThat(patientVitalDTO1).isEqualTo(patientVitalDTO2);
        patientVitalDTO2.setId(2L);
        assertThat(patientVitalDTO1).isNotEqualTo(patientVitalDTO2);
        patientVitalDTO1.setId(null);
        assertThat(patientVitalDTO1).isNotEqualTo(patientVitalDTO2);
    }
}
