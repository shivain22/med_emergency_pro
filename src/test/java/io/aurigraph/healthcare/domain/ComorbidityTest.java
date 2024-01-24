package io.aurigraph.healthcare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComorbidityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comorbidity.class);
        Comorbidity comorbidity1 = new Comorbidity();
        comorbidity1.setId(1L);
        Comorbidity comorbidity2 = new Comorbidity();
        comorbidity2.setId(comorbidity1.getId());
        assertThat(comorbidity1).isEqualTo(comorbidity2);
        comorbidity2.setId(2L);
        assertThat(comorbidity1).isNotEqualTo(comorbidity2);
        comorbidity1.setId(null);
        assertThat(comorbidity1).isNotEqualTo(comorbidity2);
    }
}
