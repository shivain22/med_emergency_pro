package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.domain.*; // for static metamodels
import io.aurigraph.healthcare.domain.PatientVital;
import io.aurigraph.healthcare.repository.PatientVitalRepository;
import io.aurigraph.healthcare.service.criteria.PatientVitalCriteria;
import io.aurigraph.healthcare.service.dto.PatientVitalDTO;
import io.aurigraph.healthcare.service.mapper.PatientVitalMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PatientVital} entities in the database.
 * The main input is a {@link PatientVitalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatientVitalDTO} or a {@link Page} of {@link PatientVitalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientVitalQueryService extends QueryService<PatientVital> {

    private final Logger log = LoggerFactory.getLogger(PatientVitalQueryService.class);

    private final PatientVitalRepository patientVitalRepository;

    private final PatientVitalMapper patientVitalMapper;

    public PatientVitalQueryService(PatientVitalRepository patientVitalRepository, PatientVitalMapper patientVitalMapper) {
        this.patientVitalRepository = patientVitalRepository;
        this.patientVitalMapper = patientVitalMapper;
    }

    /**
     * Return a {@link List} of {@link PatientVitalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatientVitalDTO> findByCriteria(PatientVitalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PatientVital> specification = createSpecification(criteria);
        return patientVitalMapper.toDto(patientVitalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PatientVitalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientVitalDTO> findByCriteria(PatientVitalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PatientVital> specification = createSpecification(criteria);
        return patientVitalRepository.findAll(specification, page).map(patientVitalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientVitalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PatientVital> specification = createSpecification(criteria);
        return patientVitalRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientVitalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PatientVital> createSpecification(PatientVitalCriteria criteria) {
        Specification<PatientVital> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PatientVital_.id));
            }
            if (criteria.getPulseRate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPulseRate(), PatientVital_.pulseRate));
            }
            if (criteria.getBloodPressure() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBloodPressure(), PatientVital_.bloodPressure));
            }
            if (criteria.getRespiration() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRespiration(), PatientVital_.respiration));
            }
            if (criteria.getSpo2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpo2(), PatientVital_.spo2));
            }
            if (criteria.getTimeOfMeasurement() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfMeasurement(), PatientVital_.timeOfMeasurement));
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPatientId(),
                            root -> root.join(PatientVital_.patient, JoinType.LEFT).get(Patient_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
