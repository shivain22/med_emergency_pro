package io.aurigraph.healthcare.service.mapper;

import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.domain.PatientVital;
import io.aurigraph.healthcare.service.dto.PatientDTO;
import io.aurigraph.healthcare.service.dto.PatientVitalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PatientVital} and its DTO {@link PatientVitalDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientVitalMapper extends EntityMapper<PatientVitalDTO, PatientVital> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientEmail")
    PatientVitalDTO toDto(PatientVital s);

    @Named("patientEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    PatientDTO toDtoPatientEmail(Patient patient);
}
