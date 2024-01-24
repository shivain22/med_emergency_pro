package io.aurigraph.healthcare.service.mapper;

import io.aurigraph.healthcare.domain.Disability;
import io.aurigraph.healthcare.service.dto.DisabilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disability} and its DTO {@link DisabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisabilityMapper extends EntityMapper<DisabilityDTO, Disability> {}
