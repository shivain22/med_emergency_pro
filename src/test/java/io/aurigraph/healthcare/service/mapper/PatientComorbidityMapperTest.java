package io.aurigraph.healthcare.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientComorbidityMapperTest {

    private PatientComorbidityMapper patientComorbidityMapper;

    @BeforeEach
    public void setUp() {
        patientComorbidityMapper = new PatientComorbidityMapperImpl();
    }
}
