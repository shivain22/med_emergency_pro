package io.aurigraph.healthcare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientDisabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientDisability.class);
        PatientDisability patientDisability1 = new PatientDisability();
        patientDisability1.setId(1L);
        PatientDisability patientDisability2 = new PatientDisability();
        patientDisability2.setId(patientDisability1.getId());
        assertThat(patientDisability1).isEqualTo(patientDisability2);
        patientDisability2.setId(2L);
        assertThat(patientDisability1).isNotEqualTo(patientDisability2);
        patientDisability1.setId(null);
        assertThat(patientDisability1).isNotEqualTo(patientDisability2);
    }
}
