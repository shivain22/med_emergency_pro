package io.aurigraph.healthcare.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PatientVital.
 */
@Entity
@Table(name = "patient_vital")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientVital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pulse_rate")
    private String pulseRate;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "respiration")
    private String respiration;

    @Column(name = "spo_2")
    private String spo2;

    @NotNull
    @Column(name = "time_of_measurement", nullable = false)
    private ZonedDateTime timeOfMeasurement;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PatientVital id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPulseRate() {
        return this.pulseRate;
    }

    public PatientVital pulseRate(String pulseRate) {
        this.setPulseRate(pulseRate);
        return this;
    }

    public void setPulseRate(String pulseRate) {
        this.pulseRate = pulseRate;
    }

    public String getBloodPressure() {
        return this.bloodPressure;
    }

    public PatientVital bloodPressure(String bloodPressure) {
        this.setBloodPressure(bloodPressure);
        return this;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getRespiration() {
        return this.respiration;
    }

    public PatientVital respiration(String respiration) {
        this.setRespiration(respiration);
        return this;
    }

    public void setRespiration(String respiration) {
        this.respiration = respiration;
    }

    public String getSpo2() {
        return this.spo2;
    }

    public PatientVital spo2(String spo2) {
        this.setSpo2(spo2);
        return this;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public ZonedDateTime getTimeOfMeasurement() {
        return this.timeOfMeasurement;
    }

    public PatientVital timeOfMeasurement(ZonedDateTime timeOfMeasurement) {
        this.setTimeOfMeasurement(timeOfMeasurement);
        return this;
    }

    public void setTimeOfMeasurement(ZonedDateTime timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientVital patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientVital)) {
            return false;
        }
        return id != null && id.equals(((PatientVital) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientVital{" +
            "id=" + getId() +
            ", pulseRate='" + getPulseRate() + "'" +
            ", bloodPressure='" + getBloodPressure() + "'" +
            ", respiration='" + getRespiration() + "'" +
            ", spo2='" + getSpo2() + "'" +
            ", timeOfMeasurement='" + getTimeOfMeasurement() + "'" +
            "}";
    }
}
