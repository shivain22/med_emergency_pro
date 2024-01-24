package io.aurigraph.healthcare.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.aurigraph.healthcare.domain.PatientVital} entity. This class is used
 * in {@link io.aurigraph.healthcare.web.rest.PatientVitalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patient-vitals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientVitalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pulseRate;

    private StringFilter bloodPressure;

    private StringFilter respiration;

    private StringFilter spo2;

    private ZonedDateTimeFilter timeOfMeasurement;

    private LongFilter patientId;

    private Boolean distinct;

    public PatientVitalCriteria() {}

    public PatientVitalCriteria(PatientVitalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pulseRate = other.pulseRate == null ? null : other.pulseRate.copy();
        this.bloodPressure = other.bloodPressure == null ? null : other.bloodPressure.copy();
        this.respiration = other.respiration == null ? null : other.respiration.copy();
        this.spo2 = other.spo2 == null ? null : other.spo2.copy();
        this.timeOfMeasurement = other.timeOfMeasurement == null ? null : other.timeOfMeasurement.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PatientVitalCriteria copy() {
        return new PatientVitalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPulseRate() {
        return pulseRate;
    }

    public StringFilter pulseRate() {
        if (pulseRate == null) {
            pulseRate = new StringFilter();
        }
        return pulseRate;
    }

    public void setPulseRate(StringFilter pulseRate) {
        this.pulseRate = pulseRate;
    }

    public StringFilter getBloodPressure() {
        return bloodPressure;
    }

    public StringFilter bloodPressure() {
        if (bloodPressure == null) {
            bloodPressure = new StringFilter();
        }
        return bloodPressure;
    }

    public void setBloodPressure(StringFilter bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public StringFilter getRespiration() {
        return respiration;
    }

    public StringFilter respiration() {
        if (respiration == null) {
            respiration = new StringFilter();
        }
        return respiration;
    }

    public void setRespiration(StringFilter respiration) {
        this.respiration = respiration;
    }

    public StringFilter getSpo2() {
        return spo2;
    }

    public StringFilter spo2() {
        if (spo2 == null) {
            spo2 = new StringFilter();
        }
        return spo2;
    }

    public void setSpo2(StringFilter spo2) {
        this.spo2 = spo2;
    }

    public ZonedDateTimeFilter getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public ZonedDateTimeFilter timeOfMeasurement() {
        if (timeOfMeasurement == null) {
            timeOfMeasurement = new ZonedDateTimeFilter();
        }
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(ZonedDateTimeFilter timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public LongFilter patientId() {
        if (patientId == null) {
            patientId = new LongFilter();
        }
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientVitalCriteria that = (PatientVitalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pulseRate, that.pulseRate) &&
            Objects.equals(bloodPressure, that.bloodPressure) &&
            Objects.equals(respiration, that.respiration) &&
            Objects.equals(spo2, that.spo2) &&
            Objects.equals(timeOfMeasurement, that.timeOfMeasurement) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pulseRate, bloodPressure, respiration, spo2, timeOfMeasurement, patientId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientVitalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pulseRate != null ? "pulseRate=" + pulseRate + ", " : "") +
            (bloodPressure != null ? "bloodPressure=" + bloodPressure + ", " : "") +
            (respiration != null ? "respiration=" + respiration + ", " : "") +
            (spo2 != null ? "spo2=" + spo2 + ", " : "") +
            (timeOfMeasurement != null ? "timeOfMeasurement=" + timeOfMeasurement + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
