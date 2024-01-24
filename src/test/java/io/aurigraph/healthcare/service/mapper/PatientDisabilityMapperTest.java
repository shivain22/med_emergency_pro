package io.aurigraph.healthcare.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientDisabilityMapperTest {

    private PatientDisabilityMapper patientDisabilityMapper;

    @BeforeEach
    public void setUp() {
        patientDisabilityMapper = new PatientDisabilityMapperImpl();
    }
}
