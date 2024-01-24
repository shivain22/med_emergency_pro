package io.aurigraph.healthcare.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.aurigraph.healthcare.domain.PatientDisability} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientDisabilityDTO implements Serializable {

    private Long id;

    private PatientDTO patient;

    private DisabilityDTO disability;

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

    public DisabilityDTO getDisability() {
        return disability;
    }

    public void setDisability(DisabilityDTO disability) {
        this.disability = disability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientDisabilityDTO)) {
            return false;
        }

        PatientDisabilityDTO patientDisabilityDTO = (PatientDisabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientDisabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDisabilityDTO{" +
            "id=" + getId() +
            ", patient=" + getPatient() +
            ", disability=" + getDisability() +
            "}";
    }
}
