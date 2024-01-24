package io.aurigraph.healthcare.repository;

import io.aurigraph.healthcare.domain.PatientVital;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PatientVital entity.
 */
@Repository
public interface PatientVitalRepository extends JpaRepository<PatientVital, Long>, JpaSpecificationExecutor<PatientVital> {
    default Optional<PatientVital> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PatientVital> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PatientVital> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct patientVital from PatientVital patientVital left join fetch patientVital.patient",
        countQuery = "select count(distinct patientVital) from PatientVital patientVital"
    )
    Page<PatientVital> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct patientVital from PatientVital patientVital left join fetch patientVital.patient")
    List<PatientVital> findAllWithToOneRelationships();

    @Query("select patientVital from PatientVital patientVital left join fetch patientVital.patient where patientVital.id =:id")
    Optional<PatientVital> findOneWithToOneRelationships(@Param("id") Long id);
}
