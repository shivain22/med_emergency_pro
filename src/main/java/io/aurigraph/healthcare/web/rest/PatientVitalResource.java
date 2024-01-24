package io.aurigraph.healthcare.web.rest;

import io.aurigraph.healthcare.repository.PatientVitalRepository;
import io.aurigraph.healthcare.service.PatientVitalQueryService;
import io.aurigraph.healthcare.service.PatientVitalService;
import io.aurigraph.healthcare.service.criteria.PatientVitalCriteria;
import io.aurigraph.healthcare.service.dto.PatientVitalDTO;
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
 * REST controller for managing {@link io.aurigraph.healthcare.domain.PatientVital}.
 */
@RestController
@RequestMapping("/api")
public class PatientVitalResource {

    private final Logger log = LoggerFactory.getLogger(PatientVitalResource.class);

    private static final String ENTITY_NAME = "patientVital";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientVitalService patientVitalService;

    private final PatientVitalRepository patientVitalRepository;

    private final PatientVitalQueryService patientVitalQueryService;

    public PatientVitalResource(
        PatientVitalService patientVitalService,
        PatientVitalRepository patientVitalRepository,
        PatientVitalQueryService patientVitalQueryService
    ) {
        this.patientVitalService = patientVitalService;
        this.patientVitalRepository = patientVitalRepository;
        this.patientVitalQueryService = patientVitalQueryService;
    }

    /**
     * {@code POST  /patient-vitals} : Create a new patientVital.
     *
     * @param patientVitalDTO the patientVitalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientVitalDTO, or with status {@code 400 (Bad Request)} if the patientVital has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-vitals")
    public ResponseEntity<PatientVitalDTO> createPatientVital(@Valid @RequestBody PatientVitalDTO patientVitalDTO)
        throws URISyntaxException {
        log.debug("REST request to save PatientVital : {}", patientVitalDTO);
        if (patientVitalDTO.getId() != null) {
            throw new BadRequestAlertException("A new patientVital cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientVitalDTO result = patientVitalService.save(patientVitalDTO);
        return ResponseEntity
            .created(new URI("/api/patient-vitals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patient-vitals/:id} : Updates an existing patientVital.
     *
     * @param id the id of the patientVitalDTO to save.
     * @param patientVitalDTO the patientVitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientVitalDTO,
     * or with status {@code 400 (Bad Request)} if the patientVitalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientVitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-vitals/{id}")
    public ResponseEntity<PatientVitalDTO> updatePatientVital(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PatientVitalDTO patientVitalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PatientVital : {}, {}", id, patientVitalDTO);
        if (patientVitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientVitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientVitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatientVitalDTO result = patientVitalService.update(patientVitalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientVitalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patient-vitals/:id} : Partial updates given fields of an existing patientVital, field will ignore if it is null
     *
     * @param id the id of the patientVitalDTO to save.
     * @param patientVitalDTO the patientVitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientVitalDTO,
     * or with status {@code 400 (Bad Request)} if the patientVitalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patientVitalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientVitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patient-vitals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientVitalDTO> partialUpdatePatientVital(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PatientVitalDTO patientVitalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientVital partially : {}, {}", id, patientVitalDTO);
        if (patientVitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientVitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientVitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientVitalDTO> result = patientVitalService.partialUpdate(patientVitalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientVitalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patient-vitals} : get all the patientVitals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientVitals in body.
     */
    @GetMapping("/patient-vitals")
    public ResponseEntity<List<PatientVitalDTO>> getAllPatientVitals(
        PatientVitalCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PatientVitals by criteria: {}", criteria);
        Page<PatientVitalDTO> page = patientVitalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patient-vitals/count} : count all the patientVitals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/patient-vitals/count")
    public ResponseEntity<Long> countPatientVitals(PatientVitalCriteria criteria) {
        log.debug("REST request to count PatientVitals by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientVitalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patient-vitals/:id} : get the "id" patientVital.
     *
     * @param id the id of the patientVitalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientVitalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-vitals/{id}")
    public ResponseEntity<PatientVitalDTO> getPatientVital(@PathVariable Long id) {
        log.debug("REST request to get PatientVital : {}", id);
        Optional<PatientVitalDTO> patientVitalDTO = patientVitalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientVitalDTO);
    }

    /**
     * {@code DELETE  /patient-vitals/:id} : delete the "id" patientVital.
     *
     * @param id the id of the patientVitalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-vitals/{id}")
    public ResponseEntity<Void> deletePatientVital(@PathVariable Long id) {
        log.debug("REST request to delete PatientVital : {}", id);
        patientVitalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
