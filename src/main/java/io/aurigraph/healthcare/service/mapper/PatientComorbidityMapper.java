package io.aurigraph.healthcare.service.mapper;

import io.aurigraph.healthcare.domain.Comorbidity;
import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.domain.PatientComorbidity;
import io.aurigraph.healthcare.service.dto.ComorbidityDTO;
import io.aurigraph.healthcare.service.dto.PatientComorbidityDTO;
import io.aurigraph.healthcare.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PatientComorbidity} and its DTO {@link PatientComorbidityDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientComorbidityMapper extends EntityMapper<PatientComorbidityDTO, PatientComorbidity> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientEmail")
    @Mapping(target = "comorbidity", source = "comorbidity", qualifiedByName = "comorbidityName")
    PatientComorbidityDTO toDto(PatientComorbidity s);

    @Named("patientEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    PatientDTO toDtoPatientEmail(Patient patient);

    @Named("comorbidityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ComorbidityDTO toDtoComorbidityName(Comorbidity comorbidity);
}
