package io.aurigraph.healthcare.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientDisabilityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientDisabilityDTO.class);
        PatientDisabilityDTO patientDisabilityDTO1 = new PatientDisabilityDTO();
        patientDisabilityDTO1.setId(1L);
        PatientDisabilityDTO patientDisabilityDTO2 = new PatientDisabilityDTO();
        assertThat(patientDisabilityDTO1).isNotEqualTo(patientDisabilityDTO2);
        patientDisabilityDTO2.setId(patientDisabilityDTO1.getId());
        assertThat(patientDisabilityDTO1).isEqualTo(patientDisabilityDTO2);
        patientDisabilityDTO2.setId(2L);
        assertThat(patientDisabilityDTO1).isNotEqualTo(patientDisabilityDTO2);
        patientDisabilityDTO1.setId(null);
        assertThat(patientDisabilityDTO1).isNotEqualTo(patientDisabilityDTO2);
    }
}
