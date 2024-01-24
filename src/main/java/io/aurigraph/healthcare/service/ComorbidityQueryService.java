package io.aurigraph.healthcare.service;

import io.aurigraph.healthcare.domain.*; // for static metamodels
import io.aurigraph.healthcare.domain.Comorbidity;
import io.aurigraph.healthcare.repository.ComorbidityRepository;
import io.aurigraph.healthcare.service.criteria.ComorbidityCriteria;
import io.aurigraph.healthcare.service.dto.ComorbidityDTO;
import io.aurigraph.healthcare.service.mapper.ComorbidityMapper;
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
 * Service for executing complex queries for {@link Comorbidity} entities in the database.
 * The main input is a {@link ComorbidityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComorbidityDTO} or a {@link Page} of {@link ComorbidityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComorbidityQueryService extends QueryService<Comorbidity> {

    private final Logger log = LoggerFactory.getLogger(ComorbidityQueryService.class);

    private final ComorbidityRepository comorbidityRepository;

    private final ComorbidityMapper comorbidityMapper;

    public ComorbidityQueryService(ComorbidityRepository comorbidityRepository, ComorbidityMapper comorbidityMapper) {
        this.comorbidityRepository = comorbidityRepository;
        this.comorbidityMapper = comorbidityMapper;
    }

    /**
     * Return a {@link List} of {@link ComorbidityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComorbidityDTO> findByCriteria(ComorbidityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comorbidity> specification = createSpecification(criteria);
        return comorbidityMapper.toDto(comorbidityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ComorbidityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComorbidityDTO> findByCriteria(ComorbidityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comorbidity> specification = createSpecification(criteria);
        return comorbidityRepository.findAll(specification, page).map(comorbidityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComorbidityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Comorbidity> specification = createSpecification(criteria);
        return comorbidityRepository.count(specification);
    }

    /**
     * Function to convert {@link ComorbidityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comorbidity> createSpecification(ComorbidityCriteria criteria) {
        Specification<Comorbidity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Comorbidity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Comorbidity_.name));
            }
        }
        return specification;
    }
}
