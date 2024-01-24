package io.aurigraph.healthcare.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.aurigraph.healthcare.domain.PatientVital} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientVitalDTO implements Serializable {

    private Long id;

    private String pulseRate;

    private String bloodPressure;

    private String respiration;

    private String spo2;

    @NotNull
    private ZonedDateTime timeOfMeasurement;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(String pulseRate) {
        this.pulseRate = pulseRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getRespiration() {
        return respiration;
    }

    public void setRespiration(String respiration) {
        this.respiration = respiration;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public ZonedDateTime getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(ZonedDateTime timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientVitalDTO)) {
            return false;
        }

        PatientVitalDTO patientVitalDTO = (PatientVitalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientVitalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientVitalDTO{" +
            "id=" + getId() +
            ", pulseRate='" + getPulseRate() + "'" +
            ", bloodPressure='" + getBloodPressure() + "'" +
            ", respiration='" + getRespiration() + "'" +
            ", spo2='" + getSpo2() + "'" +
            ", timeOfMeasurement='" + getTimeOfMeasurement() + "'" +
            ", patient=" + getPatient() +
            "}";
    }
}
