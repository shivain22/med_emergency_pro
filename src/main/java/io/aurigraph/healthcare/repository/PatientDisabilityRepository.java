package io.aurigraph.healthcare.repository;

import io.aurigraph.healthcare.domain.PatientDisability;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PatientDisability entity.
 */
@Repository
public interface PatientDisabilityRepository extends JpaRepository<PatientDisability, Long>, JpaSpecificationExecutor<PatientDisability> {
    default Optional<PatientDisability> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PatientDisability> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PatientDisability> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct patientDisability from PatientDisability patientDisability left join fetch patientDisability.patient left join fetch patientDisability.disability",
        countQuery = "select count(distinct patientDisability) from PatientDisability patientDisability"
    )
    Page<PatientDisability> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct patientDisability from PatientDisability patientDisability left join fetch patientDisability.patient left join fetch patientDisability.disability"
    )
    List<PatientDisability> findAllWithToOneRelationships();

    @Query(
        "select patientDisability from PatientDisability patientDisability left join fetch patientDisability.patient left join fetch patientDisability.disability where patientDisability.id =:id"
    )
    Optional<PatientDisability> findOneWithToOneRelationships(@Param("id") Long id);
}
