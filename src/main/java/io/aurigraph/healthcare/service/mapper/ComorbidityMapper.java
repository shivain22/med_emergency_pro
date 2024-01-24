package io.aurigraph.healthcare.service.mapper;

import io.aurigraph.healthcare.domain.Comorbidity;
import io.aurigraph.healthcare.service.dto.ComorbidityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comorbidity} and its DTO {@link ComorbidityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComorbidityMapper extends EntityMapper<ComorbidityDTO, Comorbidity> {}
