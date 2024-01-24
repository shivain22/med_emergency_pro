package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.service.dto.PatientDisabilityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.aurigraph.healthcare.domain.PatientDisability}.
 */
public interface PatientDisabilityService {
    /**
     * Save a patientDisability.
     *
     * @param patientDisabilityDTO the entity to save.
     * @return the persisted entity.
     */
    PatientDisabilityDTO save(PatientDisabilityDTO patientDisabilityDTO);

    /**
     * Updates a patientDisability.
     *
     * @param patientDisabilityDTO the entity to update.
     * @return the persisted entity.
     */
    PatientDisabilityDTO update(PatientDisabilityDTO patientDisabilityDTO);

    /**
     * Partially updates a patientDisability.
     *
     * @param patientDisabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PatientDisabilityDTO> partialUpdate(PatientDisabilityDTO patientDisabilityDTO);

    /**
     * Get all the patientDisabilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatientDisabilityDTO> findAll(Pageable pageable);

    /**
     * Get all the patientDisabilities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatientDisabilityDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" patientDisability.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PatientDisabilityDTO> findOne(Long id);

    /**
     * Delete the "id" patientDisability.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
