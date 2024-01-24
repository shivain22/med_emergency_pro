package io.aurigraph.healthcare.web.rest;

import io.aurigraph.healthcare.repository.PatientDisabilityRepository;
import io.aurigraph.healthcare.service.PatientDisabilityQueryService;
import io.aurigraph.healthcare.service.PatientDisabilityService;
import io.aurigraph.healthcare.service.criteria.PatientDisabilityCriteria;
import io.aurigraph.healthcare.service.dto.PatientDisabilityDTO;
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
 * REST controller for managing {@link io.aurigraph.healthcare.domain.PatientDisability}.
 */
@RestController
@RequestMapping("/api")
public class PatientDisabilityResource {

    private final Logger log = LoggerFactory.getLogger(PatientDisabilityResource.class);

    private static final String ENTITY_NAME = "patientDisability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientDisabilityService patientDisabilityService;

    private final PatientDisabilityRepository patientDisabilityRepository;

    private final PatientDisabilityQueryService patientDisabilityQueryService;

    public PatientDisabilityResource(
        PatientDisabilityService patientDisabilityService,
        PatientDisabilityRepository patientDisabilityRepository,
        PatientDisabilityQueryService patientDisabilityQueryService
    ) {
        this.patientDisabilityService = patientDisabilityService;
        this.patientDisabilityRepository = patientDisabilityRepository;
        this.patientDisabilityQueryService = patientDisabilityQueryService;
    }

    /**
     * {@code POST  /patient-disabilities} : Create a new patientDisability.
     *
     * @param patientDisabilityDTO the patientDisabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientDisabilityDTO, or with status {@code 400 (Bad Request)} if the patientDisability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-disabilities")
    public ResponseEntity<PatientDisabilityDTO> createPatientDisability(@Valid @RequestBody PatientDisabilityDTO patientDisabilityDTO)
        throws URISyntaxException {
        log.debug("REST request to save PatientDisability : {}", patientDisabilityDTO);
        if (patientDisabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new patientDisability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientDisabilityDTO result = patientDisabilityService.save(patientDisabilityDTO);
        return ResponseEntity
            .created(new URI("/api/patient-disabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patient-disabilities/:id} : Updates an existing patientDisability.
     *
     * @param id the id of the patientDisabilityDTO to save.
     * @param patientDisabilityDTO the patientDisabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientDisabilityDTO,
     * or with status {@code 400 (Bad Request)} if the patientDisabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientDisabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-disabilities/{id}")
    public ResponseEntity<PatientDisabilityDTO> updatePatientDisability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PatientDisabilityDTO patientDisabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PatientDisability : {}, {}", id, patientDisabilityDTO);
        if (patientDisabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientDisabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientDisabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatientDisabilityDTO result = patientDisabilityService.update(patientDisabilityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientDisabilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patient-disabilities/:id} : Partial updates given fields of an existing patientDisability, field will ignore if it is null
     *
     * @param id the id of the patientDisabilityDTO to save.
     * @param patientDisabilityDTO the patientDisabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientDisabilityDTO,
     * or with status {@code 400 (Bad Request)} if the patientDisabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patientDisabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientDisabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patient-disabilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientDisabilityDTO> partialUpdatePatientDisability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PatientDisabilityDTO patientDisabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientDisability partially : {}, {}", id, patientDisabilityDTO);
        if (patientDisabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientDisabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientDisabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientDisabilityDTO> result = patientDisabilityService.partialUpdate(patientDisabilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientDisabilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patient-disabilities} : get all the patientDisabilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientDisabilities in body.
     */
    @GetMapping("/patient-disabilities")
    public ResponseEntity<List<PatientDisabilityDTO>> getAllPatientDisabilities(
        PatientDisabilityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PatientDisabilities by criteria: {}", criteria);
        Page<PatientDisabilityDTO> page = patientDisabilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patient-disabilities/count} : count all the patientDisabilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/patient-disabilities/count")
    public ResponseEntity<Long> countPatientDisabilities(PatientDisabilityCriteria criteria) {
        log.debug("REST request to count PatientDisabilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientDisabilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patient-disabilities/:id} : get the "id" patientDisability.
     *
     * @param id the id of the patientDisabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientDisabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-disabilities/{id}")
    public ResponseEntity<PatientDisabilityDTO> getPatientDisability(@PathVariable Long id) {
        log.debug("REST request to get PatientDisability : {}", id);
        Optional<PatientDisabilityDTO> patientDisabilityDTO = patientDisabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientDisabilityDTO);
    }

    /**
     * {@code DELETE  /patient-disabilities/:id} : delete the "id" patientDisability.
     *
     * @param id the id of the patientDisabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-disabilities/{id}")
    public ResponseEntity<Void> deletePatientDisability(@PathVariable Long id) {
        log.debug("REST request to delete PatientDisability : {}", id);
        patientDisabilityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
