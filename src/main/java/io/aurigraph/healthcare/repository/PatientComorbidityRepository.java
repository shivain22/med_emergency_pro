package io.aurigraph.healthcare.repository;

import io.aurigraph.healthcare.domain.PatientComorbidity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PatientComorbidity entity.
 */
@Repository
public interface PatientComorbidityRepository
    extends JpaRepository<PatientComorbidity, Long>, JpaSpecificationExecutor<PatientComorbidity> {
    default Optional<PatientComorbidity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PatientComorbidity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PatientComorbidity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct patientComorbidity from PatientComorbidity patientComorbidity left join fetch patientComorbidity.patient left join fetch patientComorbidity.comorbidity",
        countQuery = "select count(distinct patientComorbidity) from PatientComorbidity patientComorbidity"
    )
    Page<PatientComorbidity> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct patientComorbidity from PatientComorbidity patientComorbidity left join fetch patientComorbidity.patient left join fetch patientComorbidity.comorbidity"
    )
    List<PatientComorbidity> findAllWithToOneRelationships();

    @Query(
        "select patientComorbidity from PatientComorbidity patientComorbidity left join fetch patientComorbidity.patient left join fetch patientComorbidity.comorbidity where patientComorbidity.id =:id"
    )
    Optional<PatientComorbidity> findOneWithToOneRelationships(@Param("id") Long id);
}
