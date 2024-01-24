package io.aurigraph.healthcare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientVitalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientVital.class);
        PatientVital patientVital1 = new PatientVital();
        patientVital1.setId(1L);
        PatientVital patientVital2 = new PatientVital();
        patientVital2.setId(patientVital1.getId());
        assertThat(patientVital1).isEqualTo(patientVital2);
        patientVital2.setId(2L);
        assertThat(patientVital1).isNotEqualTo(patientVital2);
        patientVital1.setId(null);
        assertThat(patientVital1).isNotEqualTo(patientVital2);
    }
}
