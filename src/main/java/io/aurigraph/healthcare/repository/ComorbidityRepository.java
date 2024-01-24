package io.aurigraph.healthcare.repository;

import io.aurigraph.healthcare.domain.Comorbidity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Comorbidity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComorbidityRepository extends JpaRepository<Comorbidity, Long>, JpaSpecificationExecutor<Comorbidity> {}
