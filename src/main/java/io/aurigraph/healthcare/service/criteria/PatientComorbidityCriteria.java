package io.aurigraph.healthcare.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.aurigraph.healthcare.domain.PatientComorbidity} entity. This class is used
 * in {@link io.aurigraph.healthcare.web.rest.PatientComorbidityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patient-comorbidities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientComorbidityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter patientId;

    private LongFilter comorbidityId;

    private Boolean distinct;

    public PatientComorbidityCriteria() {}

    public PatientComorbidityCriteria(PatientComorbidityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.comorbidityId = other.comorbidityId == null ? null : other.comorbidityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PatientComorbidityCriteria copy() {
        return new PatientComorbidityCriteria(this);
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

    public LongFilter getComorbidityId() {
        return comorbidityId;
    }

    public LongFilter comorbidityId() {
        if (comorbidityId == null) {
            comorbidityId = new LongFilter();
        }
        return comorbidityId;
    }

    public void setComorbidityId(LongFilter comorbidityId) {
        this.comorbidityId = comorbidityId;
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
        final PatientComorbidityCriteria that = (PatientComorbidityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(comorbidityId, that.comorbidityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, comorbidityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientComorbidityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            (comorbidityId != null ? "comorbidityId=" + comorbidityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
