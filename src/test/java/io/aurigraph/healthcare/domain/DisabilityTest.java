package io.aurigraph.healthcare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.aurigraph.healthcare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disability.class);
        Disability disability1 = new Disability();
        disability1.setId(1L);
        Disability disability2 = new Disability();
        disability2.setId(disability1.getId());
        assertThat(disability1).isEqualTo(disability2);
        disability2.setId(2L);
        assertThat(disability1).isNotEqualTo(disability2);
        disability1.setId(null);
        assertThat(disability1).isNotEqualTo(disability2);
    }
}
