package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.domain.*; // for static metamodels
import io.aurigraph.healthcare.domain.PatientDisability;
import io.aurigraph.healthcare.repository.PatientDisabilityRepository;
import io.aurigraph.healthcare.service.criteria.PatientDisabilityCriteria;
import io.aurigraph.healthcare.service.dto.PatientDisabilityDTO;
import io.aurigraph.healthcare.service.mapper.PatientDisabilityMapper;
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
 * Service for executing complex queries for {@link PatientDisability} entities in the database.
 * The main input is a {@link PatientDisabilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatientDisabilityDTO} or a {@link Page} of {@link PatientDisabilityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientDisabilityQueryService extends QueryService<PatientDisability> {

    private final Logger log = LoggerFactory.getLogger(PatientDisabilityQueryService.class);

    private final PatientDisabilityRepository patientDisabilityRepository;

    private final PatientDisabilityMapper patientDisabilityMapper;

    public PatientDisabilityQueryService(
        PatientDisabilityRepository patientDisabilityRepository,
        PatientDisabilityMapper patientDisabilityMapper
    ) {
        this.patientDisabilityRepository = patientDisabilityRepository;
        this.patientDisabilityMapper = patientDisabilityMapper;
    }

    /**
     * Return a {@link List} of {@link PatientDisabilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatientDisabilityDTO> findByCriteria(PatientDisabilityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PatientDisability> specification = createSpecification(criteria);
        return patientDisabilityMapper.toDto(patientDisabilityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PatientDisabilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientDisabilityDTO> findByCriteria(PatientDisabilityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PatientDisability> specification = createSpecification(criteria);
        return patientDisabilityRepository.findAll(specification, page).map(patientDisabilityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientDisabilityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PatientDisability> specification = createSpecification(criteria);
        return patientDisabilityRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientDisabilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PatientDisability> createSpecification(PatientDisabilityCriteria criteria) {
        Specification<PatientDisability> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PatientDisability_.id));
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPatientId(),
                            root -> root.join(PatientDisability_.patient, JoinType.LEFT).get(Patient_.id)
                        )
                    );
            }
            if (criteria.getDisabilityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDisabilityId(),
                            root -> root.join(PatientDisability_.disability, JoinType.LEFT).get(Disability_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
