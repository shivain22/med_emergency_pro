package io.aurigraph.healthcare.web.rest;

import io.aurigraph.healthcare.repository.ComorbidityRepository;
import io.aurigraph.healthcare.service.ComorbidityQueryService;
import io.aurigraph.healthcare.service.ComorbidityService;
import io.aurigraph.healthcare.service.criteria.ComorbidityCriteria;
import io.aurigraph.healthcare.service.dto.ComorbidityDTO;
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
 * REST controller for managing {@link io.aurigraph.healthcare.domain.Comorbidity}.
 */
@RestController
@RequestMapping("/api")
public class ComorbidityResource {

    private final Logger log = LoggerFactory.getLogger(ComorbidityResource.class);

    private static final String ENTITY_NAME = "comorbidity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComorbidityService comorbidityService;

    private final ComorbidityRepository comorbidityRepository;

    private final ComorbidityQueryService comorbidityQueryService;

    public ComorbidityResource(
        ComorbidityService comorbidityService,
        ComorbidityRepository comorbidityRepository,
        ComorbidityQueryService comorbidityQueryService
    ) {
        this.comorbidityService = comorbidityService;
        this.comorbidityRepository = comorbidityRepository;
        this.comorbidityQueryService = comorbidityQueryService;
    }

    /**
     * {@code POST  /comorbidities} : Create a new comorbidity.
     *
     * @param comorbidityDTO the comorbidityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comorbidityDTO, or with status {@code 400 (Bad Request)} if the comorbidity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comorbidities")
    public ResponseEntity<ComorbidityDTO> createComorbidity(@Valid @RequestBody ComorbidityDTO comorbidityDTO) throws URISyntaxException {
        log.debug("REST request to save Comorbidity : {}", comorbidityDTO);
        if (comorbidityDTO.getId() != null) {
            throw new BadRequestAlertException("A new comorbidity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComorbidityDTO result = comorbidityService.save(comorbidityDTO);
        return ResponseEntity
            .created(new URI("/api/comorbidities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comorbidities/:id} : Updates an existing comorbidity.
     *
     * @param id the id of the comorbidityDTO to save.
     * @param comorbidityDTO the comorbidityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comorbidityDTO,
     * or with status {@code 400 (Bad Request)} if the comorbidityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comorbidityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comorbidities/{id}")
    public ResponseEntity<ComorbidityDTO> updateComorbidity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComorbidityDTO comorbidityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Comorbidity : {}, {}", id, comorbidityDTO);
        if (comorbidityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comorbidityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comorbidityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComorbidityDTO result = comorbidityService.update(comorbidityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comorbidityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comorbidities/:id} : Partial updates given fields of an existing comorbidity, field will ignore if it is null
     *
     * @param id the id of the comorbidityDTO to save.
     * @param comorbidityDTO the comorbidityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comorbidityDTO,
     * or with status {@code 400 (Bad Request)} if the comorbidityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the comorbidityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the comorbidityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comorbidities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComorbidityDTO> partialUpdateComorbidity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComorbidityDTO comorbidityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Comorbidity partially : {}, {}", id, comorbidityDTO);
        if (comorbidityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comorbidityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comorbidityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComorbidityDTO> result = comorbidityService.partialUpdate(comorbidityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comorbidityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comorbidities} : get all the comorbidities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comorbidities in body.
     */
    @GetMapping("/comorbidities")
    public ResponseEntity<List<ComorbidityDTO>> getAllComorbidities(
        ComorbidityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Comorbidities by criteria: {}", criteria);
        Page<ComorbidityDTO> page = comorbidityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comorbidities/count} : count all the comorbidities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/comorbidities/count")
    public ResponseEntity<Long> countComorbidities(ComorbidityCriteria criteria) {
        log.debug("REST request to count Comorbidities by criteria: {}", criteria);
        return ResponseEntity.ok().body(comorbidityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /comorbidities/:id} : get the "id" comorbidity.
     *
     * @param id the id of the comorbidityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comorbidityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comorbidities/{id}")
    public ResponseEntity<ComorbidityDTO> getComorbidity(@PathVariable Long id) {
        log.debug("REST request to get Comorbidity : {}", id);
        Optional<ComorbidityDTO> comorbidityDTO = comorbidityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comorbidityDTO);
    }

    /**
     * {@code DELETE  /comorbidities/:id} : delete the "id" comorbidity.
     *
     * @param id the id of the comorbidityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comorbidities/{id}")
    public ResponseEntity<Void> deleteComorbidity(@PathVariable Long id) {
        log.debug("REST request to delete Comorbidity : {}", id);
        comorbidityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
