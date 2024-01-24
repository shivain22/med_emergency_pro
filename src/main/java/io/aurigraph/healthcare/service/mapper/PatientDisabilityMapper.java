package io.aurigraph.healthcare.service.mapper;

import io.aurigraph.healthcare.domain.Disability;
import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.domain.PatientDisability;
import io.aurigraph.healthcare.service.dto.DisabilityDTO;
import io.aurigraph.healthcare.service.dto.PatientDTO;
import io.aurigraph.healthcare.service.dto.PatientDisabilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PatientDisability} and its DTO {@link PatientDisabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientDisabilityMapper extends EntityMapper<PatientDisabilityDTO, PatientDisability> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientEmail")
    @Mapping(target = "disability", source = "disability", qualifiedByName = "disabilityName")
    PatientDisabilityDTO toDto(PatientDisability s);

    @Named("patientEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    PatientDTO toDtoPatientEmail(Patient patient);

    @Named("disabilityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DisabilityDTO toDtoDisabilityName(Disability disability);
}
