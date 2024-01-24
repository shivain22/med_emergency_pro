package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.service.dto.ComorbidityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.aurigraph.healthcare.domain.Comorbidity}.
 */
public interface ComorbidityService {
    /**
     * Save a comorbidity.
     *
     * @param comorbidityDTO the entity to save.
     * @return the persisted entity.
     */
    ComorbidityDTO save(ComorbidityDTO comorbidityDTO);

    /**
     * Updates a comorbidity.
     *
     * @param comorbidityDTO the entity to update.
     * @return the persisted entity.
     */
    ComorbidityDTO update(ComorbidityDTO comorbidityDTO);

    /**
     * Partially updates a comorbidity.
     *
     * @param comorbidityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ComorbidityDTO> partialUpdate(ComorbidityDTO comorbidityDTO);

    /**
     * Get all the comorbidities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComorbidityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" comorbidity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComorbidityDTO> findOne(Long id);

    /**
     * Delete the "id" comorbidity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
