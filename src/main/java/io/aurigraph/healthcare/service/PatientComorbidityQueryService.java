package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.domain.*; // for static metamodels
import io.aurigraph.healthcare.domain.PatientComorbidity;
import io.aurigraph.healthcare.repository.PatientComorbidityRepository;
import io.aurigraph.healthcare.service.criteria.PatientComorbidityCriteria;
import io.aurigraph.healthcare.service.dto.PatientComorbidityDTO;
import io.aurigraph.healthcare.service.mapper.PatientComorbidityMapper;
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
 * Service for executing complex queries for {@link PatientComorbidity} entities in the database.
 * The main input is a {@link PatientComorbidityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatientComorbidityDTO} or a {@link Page} of {@link PatientComorbidityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientComorbidityQueryService extends QueryService<PatientComorbidity> {

    private final Logger log = LoggerFactory.getLogger(PatientComorbidityQueryService.class);

    private final PatientComorbidityRepository patientComorbidityRepository;

    private final PatientComorbidityMapper patientComorbidityMapper;

    public PatientComorbidityQueryService(
        PatientComorbidityRepository patientComorbidityRepository,
        PatientComorbidityMapper patientComorbidityMapper
    ) {
        this.patientComorbidityRepository = patientComorbidityRepository;
        this.patientComorbidityMapper = patientComorbidityMapper;
    }

    /**
     * Return a {@link List} of {@link PatientComorbidityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatientComorbidityDTO> findByCriteria(PatientComorbidityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PatientComorbidity> specification = createSpecification(criteria);
        return patientComorbidityMapper.toDto(patientComorbidityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PatientComorbidityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientComorbidityDTO> findByCriteria(PatientComorbidityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PatientComorbidity> specification = createSpecification(criteria);
        return patientComorbidityRepository.findAll(specification, page).map(patientComorbidityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientComorbidityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PatientComorbidity> specification = createSpecification(criteria);
        return patientComorbidityRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientComorbidityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PatientComorbidity> createSpecification(PatientComorbidityCriteria criteria) {
        Specification<PatientComorbidity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PatientComorbidity_.id));
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPatientId(),
                            root -> root.join(PatientComorbidity_.patient, JoinType.LEFT).get(Patient_.id)
                        )
                    );
            }
            if (criteria.getComorbidityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComorbidityId(),
                            root -> root.join(PatientComorbidity_.comorbidity, JoinType.LEFT).get(Comorbidity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
