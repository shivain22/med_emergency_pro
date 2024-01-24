package io.aurigraph.healthcare.web.rest;

import io.aurigraph.healthcare.repository.PatientComorbidityRepository;
import io.aurigraph.healthcare.service.PatientComorbidityQueryService;
import io.aurigraph.healthcare.service.PatientComorbidityService;
import io.aurigraph.healthcare.service.criteria.PatientComorbidityCriteria;
import io.aurigraph.healthcare.service.dto.PatientComorbidityDTO;
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
 * REST controller for managing {@link io.aurigraph.healthcare.domain.PatientComorbidity}.
 */
@RestController
@RequestMapping("/api")
public class PatientComorbidityResource {

    private final Logger log = LoggerFactory.getLogger(PatientComorbidityResource.class);

    private static final String ENTITY_NAME = "patientComorbidity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientComorbidityService patientComorbidityService;

    private final PatientComorbidityRepository patientComorbidityRepository;

    private final PatientComorbidityQueryService patientComorbidityQueryService;

    public PatientComorbidityResource(
        PatientComorbidityService patientComorbidityService,
        PatientComorbidityRepository patientComorbidityRepository,
        PatientComorbidityQueryService patientComorbidityQueryService
    ) {
        this.patientComorbidityService = patientComorbidityService;
        this.patientComorbidityRepository = patientComorbidityRepository;
        this.patientComorbidityQueryService = patientComorbidityQueryService;
    }

    /**
     * {@code POST  /patient-comorbidities} : Create a new patientComorbidity.
     *
     * @param patientComorbidityDTO the patientComorbidityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientComorbidityDTO, or with status {@code 400 (Bad Request)} if the patientComorbidity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-comorbidities")
    public ResponseEntity<PatientComorbidityDTO> createPatientComorbidity(@Valid @RequestBody PatientComorbidityDTO patientComorbidityDTO)
        throws URISyntaxException {
        log.debug("REST request to save PatientComorbidity : {}", patientComorbidityDTO);
        if (patientComorbidityDTO.getId() != null) {
            throw new BadRequestAlertException("A new patientComorbidity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientComorbidityDTO result = patientComorbidityService.save(patientComorbidityDTO);
        return ResponseEntity
            .created(new URI("/api/patient-comorbidities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patient-comorbidities/:id} : Updates an existing patientComorbidity.
     *
     * @param id the id of the patientComorbidityDTO to save.
     * @param patientComorbidityDTO the patientComorbidityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientComorbidityDTO,
     * or with status {@code 400 (Bad Request)} if the patientComorbidityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientComorbidityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-comorbidities/{id}")
    public ResponseEntity<PatientComorbidityDTO> updatePatientComorbidity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PatientComorbidityDTO patientComorbidityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PatientComorbidity : {}, {}", id, patientComorbidityDTO);
        if (patientComorbidityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientComorbidityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientComorbidityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatientComorbidityDTO result = patientComorbidityService.update(patientComorbidityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientComorbidityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patient-comorbidities/:id} : Partial updates given fields of an existing patientComorbidity, field will ignore if it is null
     *
     * @param id the id of the patientComorbidityDTO to save.
     * @param patientComorbidityDTO the patientComorbidityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientComorbidityDTO,
     * or with status {@code 400 (Bad Request)} if the patientComorbidityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patientComorbidityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientComorbidityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patient-comorbidities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientComorbidityDTO> partialUpdatePatientComorbidity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PatientComorbidityDTO patientComorbidityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientComorbidity partially : {}, {}", id, patientComorbidityDTO);
        if (patientComorbidityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientComorbidityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientComorbidityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientComorbidityDTO> result = patientComorbidityService.partialUpdate(patientComorbidityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientComorbidityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patient-comorbidities} : get all the patientComorbidities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientComorbidities in body.
     */
    @GetMapping("/patient-comorbidities")
    public ResponseEntity<List<PatientComorbidityDTO>> getAllPatientComorbidities(
        PatientComorbidityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PatientComorbidities by criteria: {}", criteria);
        Page<PatientComorbidityDTO> page = patientComorbidityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patient-comorbidities/count} : count all the patientComorbidities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/patient-comorbidities/count")
    public ResponseEntity<Long> countPatientComorbidities(PatientComorbidityCriteria criteria) {
        log.debug("REST request to count PatientComorbidities by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientComorbidityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patient-comorbidities/:id} : get the "id" patientComorbidity.
     *
     * @param id the id of the patientComorbidityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientComorbidityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-comorbidities/{id}")
    public ResponseEntity<PatientComorbidityDTO> getPatientComorbidity(@PathVariable Long id) {
        log.debug("REST request to get PatientComorbidity : {}", id);
        Optional<PatientComorbidityDTO> patientComorbidityDTO = patientComorbidityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientComorbidityDTO);
    }

    /**
     * {@code DELETE  /patient-comorbidities/:id} : delete the "id" patientComorbidity.
     *
     * @param id the id of the patientComorbidityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-comorbidities/{id}")
    public ResponseEntity<Void> deletePatientComorbidity(@PathVariable Long id) {
        log.debug("REST request to delete PatientComorbidity : {}", id);
        patientComorbidityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
