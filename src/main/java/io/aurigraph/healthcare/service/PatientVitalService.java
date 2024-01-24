package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.service.dto.PatientVitalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.aurigraph.healthcare.domain.PatientVital}.
 */
public interface PatientVitalService {
    /**
     * Save a patientVital.
     *
     * @param patientVitalDTO the entity to save.
     * @return the persisted entity.
     */
    PatientVitalDTO save(PatientVitalDTO patientVitalDTO);

    /**
     * Updates a patientVital.
     *
     * @param patientVitalDTO the entity to update.
     * @return the persisted entity.
     */
    PatientVitalDTO update(PatientVitalDTO patientVitalDTO);

    /**
     * Partially updates a patientVital.
     *
     * @param patientVitalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PatientVitalDTO> partialUpdate(PatientVitalDTO patientVitalDTO);

    /**
     * Get all the patientVitals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatientVitalDTO> findAll(Pageable pageable);

    /**
     * Get all the patientVitals with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatientVitalDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" patientVital.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PatientVitalDTO> findOne(Long id);

    /**
     * Delete the "id" patientVital.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
