package io.aurigraph.healthcare.repository;

import io.aurigraph.healthcare.domain.Disability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Disability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisabilityRepository extends JpaRepository<Disability, Long>, JpaSpecificationExecutor<Disability> {}
