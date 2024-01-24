package io.aurigraph.healthcare.service.mapper;

import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Patient} and its DTO {@link PatientDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {}
