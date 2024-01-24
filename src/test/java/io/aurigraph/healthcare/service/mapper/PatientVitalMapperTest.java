package io.aurigraph.healthcare.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientVitalMapperTest {

    private PatientVitalMapper patientVitalMapper;

    @BeforeEach
    public void setUp() {
        patientVitalMapper = new PatientVitalMapperImpl();
    }
}
