package io.aurigraph.healthcare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientComorbidityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientComorbidity.class);
        PatientComorbidity patientComorbidity1 = new PatientComorbidity();
        patientComorbidity1.setId(1L);
        PatientComorbidity patientComorbidity2 = new PatientComorbidity();
        patientComorbidity2.setId(patientComorbidity1.getId());
        assertThat(patientComorbidity1).isEqualTo(patientComorbidity2);
        patientComorbidity2.setId(2L);
        assertThat(patientComorbidity1).isNotEqualTo(patientComorbidity2);
        patientComorbidity1.setId(null);
        assertThat(patientComorbidity1).isNotEqualTo(patientComorbidity2);
    }
}
