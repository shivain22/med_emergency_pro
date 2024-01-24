package io.aurigraph.healthcare.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.aurigraph.healthcare.domain.PatientComorbidity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientComorbidityDTO implements Serializable {

    private Long id;

    private PatientDTO patient;

    private ComorbidityDTO comorbidity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public ComorbidityDTO getComorbidity() {
        return comorbidity;
    }

    public void setComorbidity(ComorbidityDTO comorbidity) {
        this.comorbidity = comorbidity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientComorbidityDTO)) {
            return false;
        }

        PatientComorbidityDTO patientComorbidityDTO = (PatientComorbidityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientComorbidityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientComorbidityDTO{" +
            "id=" + getId() +
            ", patient=" + getPatient() +
            ", comorbidity=" + getComorbidity() +
            "}";
    }
}
