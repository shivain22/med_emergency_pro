package io.aurigraph.healthcare.service.mapper;

import io.aurigraph.healthcare.domain.Comment;
import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.service.dto.CommentDTO;
import io.aurigraph.healthcare.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientEmail")
    CommentDTO toDto(Comment s);

    @Named("patientEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    PatientDTO toDtoPatientEmail(Patient patient);
}
