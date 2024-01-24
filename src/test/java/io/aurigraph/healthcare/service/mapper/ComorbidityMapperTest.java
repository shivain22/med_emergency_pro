package io.aurigraph.healthcare.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComorbidityMapperTest {

    private ComorbidityMapper comorbidityMapper;

    @BeforeEach
    public void setUp() {
        comorbidityMapper = new ComorbidityMapperImpl();
    }
}
