package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.service.dto.DisabilityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.aurigraph.healthcare.domain.Disability}.
 */
public interface DisabilityService {
    /**
     * Save a disability.
     *
     * @param disabilityDTO the entity to save.
     * @return the persisted entity.
     */
    DisabilityDTO save(DisabilityDTO disabilityDTO);

    /**
     * Updates a disability.
     *
     * @param disabilityDTO the entity to update.
     * @return the persisted entity.
     */
    DisabilityDTO update(DisabilityDTO disabilityDTO);

    /**
     * Partially updates a disability.
     *
     * @param disabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DisabilityDTO> partialUpdate(DisabilityDTO disabilityDTO);

    /**
     * Get all the disabilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DisabilityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" disability.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DisabilityDTO> findOne(Long id);

    /**
     * Delete the "id" disability.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
