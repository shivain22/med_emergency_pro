package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.service.dto.PatientComorbidityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.aurigraph.healthcare.domain.PatientComorbidity}.
 */
public interface PatientComorbidityService {
    /**
     * Save a patientComorbidity.
     *
     * @param patientComorbidityDTO the entity to save.
     * @return the persisted entity.
     */
    PatientComorbidityDTO save(PatientComorbidityDTO patientComorbidityDTO);

    /**
     * Updates a patientComorbidity.
     *
     * @param patientComorbidityDTO the entity to update.
     * @return the persisted entity.
     */
    PatientComorbidityDTO update(PatientComorbidityDTO patientComorbidityDTO);

    /**
     * Partially updates a patientComorbidity.
     *
     * @param patientComorbidityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PatientComorbidityDTO> partialUpdate(PatientComorbidityDTO patientComorbidityDTO);

    /**
     * Get all the patientComorbidities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatientComorbidityDTO> findAll(Pageable pageable);

    /**
     * Get all the patientComorbidities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatientComorbidityDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" patientComorbidity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PatientComorbidityDTO> findOne(Long id);

    /**
     * Delete the "id" patientComorbidity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
