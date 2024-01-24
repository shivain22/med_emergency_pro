package io.aurigraph.healthcare.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisabilityMapperTest {

    private DisabilityMapper disabilityMapper;

    @BeforeEach
    public void setUp() {
        disabilityMapper = new DisabilityMapperImpl();
    }
}
