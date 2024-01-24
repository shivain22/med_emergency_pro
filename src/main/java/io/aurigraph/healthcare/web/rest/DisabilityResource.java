package io.aurigraph.healthcare.web.rest;

import io.aurigraph.healthcare.repository.DisabilityRepository;
import io.aurigraph.healthcare.service.DisabilityQueryService;
import io.aurigraph.healthcare.service.DisabilityService;
import io.aurigraph.healthcare.service.criteria.DisabilityCriteria;
import io.aurigraph.healthcare.service.dto.DisabilityDTO;
import io.aurigraph.healthcare.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.aurigraph.healthcare.domain.Disability}.
 */
@RestController
@RequestMapping("/api")
public class DisabilityResource {

    private final Logger log = LoggerFactory.getLogger(DisabilityResource.class);

    private static final String ENTITY_NAME = "disability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisabilityService disabilityService;

    private final DisabilityRepository disabilityRepository;

    private final DisabilityQueryService disabilityQueryService;

    public DisabilityResource(
        DisabilityService disabilityService,
        DisabilityRepository disabilityRepository,
        DisabilityQueryService disabilityQueryService
    ) {
        this.disabilityService = disabilityService;
        this.disabilityRepository = disabilityRepository;
        this.disabilityQueryService = disabilityQueryService;
    }

    /**
     * {@code POST  /disabilities} : Create a new disability.
     *
     * @param disabilityDTO the disabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disabilityDTO, or with status {@code 400 (Bad Request)} if the disability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/disabilities")
    public ResponseEntity<DisabilityDTO> createDisability(@Valid @RequestBody DisabilityDTO disabilityDTO) throws URISyntaxException {
        log.debug("REST request to save Disability : {}", disabilityDTO);
        if (disabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new disability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DisabilityDTO result = disabilityService.save(disabilityDTO);
        return ResponseEntity
            .created(new URI("/api/disabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /disabilities/:id} : Updates an existing disability.
     *
     * @param id the id of the disabilityDTO to save.
     * @param disabilityDTO the disabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disabilityDTO,
     * or with status {@code 400 (Bad Request)} if the disabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/disabilities/{id}")
    public ResponseEntity<DisabilityDTO> updateDisability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DisabilityDTO disabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Disability : {}, {}", id, disabilityDTO);
        if (disabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DisabilityDTO result = disabilityService.update(disabilityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disabilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /disabilities/:id} : Partial updates given fields of an existing disability, field will ignore if it is null
     *
     * @param id the id of the disabilityDTO to save.
     * @param disabilityDTO the disabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disabilityDTO,
     * or with status {@code 400 (Bad Request)} if the disabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the disabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the disabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/disabilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisabilityDTO> partialUpdateDisability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DisabilityDTO disabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Disability partially : {}, {}", id, disabilityDTO);
        if (disabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisabilityDTO> result = disabilityService.partialUpdate(disabilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disabilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /disabilities} : get all the disabilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disabilities in body.
     */
    @GetMapping("/disabilities")
    public ResponseEntity<List<DisabilityDTO>> getAllDisabilities(
        DisabilityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Disabilities by criteria: {}", criteria);
        Page<DisabilityDTO> page = disabilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /disabilities/count} : count all the disabilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/disabilities/count")
    public ResponseEntity<Long> countDisabilities(DisabilityCriteria criteria) {
        log.debug("REST request to count Disabilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(disabilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /disabilities/:id} : get the "id" disability.
     *
     * @param id the id of the disabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/disabilities/{id}")
    public ResponseEntity<DisabilityDTO> getDisability(@PathVariable Long id) {
        log.debug("REST request to get Disability : {}", id);
        Optional<DisabilityDTO> disabilityDTO = disabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disabilityDTO);
    }

    /**
     * {@code DELETE  /disabilities/:id} : delete the "id" disability.
     *
     * @param id the id of the disabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/disabilities/{id}")
    public ResponseEntity<Void> deleteDisability(@PathVariable Long id) {
        log.debug("REST request to delete Disability : {}", id);
        disabilityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
