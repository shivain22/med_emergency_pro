package io.aurigraph.healthcare.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.aurigraph.healthcare.domain.PatientDisability} entity. This class is used
 * in {@link io.aurigraph.healthcare.web.rest.PatientDisabilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patient-disabilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientDisabilityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter patientId;

    private LongFilter disabilityId;

    private Boolean distinct;

    public PatientDisabilityCriteria() {}

    public PatientDisabilityCriteria(PatientDisabilityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.disabilityId = other.disabilityId == null ? null : other.disabilityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PatientDisabilityCriteria copy() {
        return new PatientDisabilityCriteria(this);
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

    public LongFilter getDisabilityId() {
        return disabilityId;
    }

    public LongFilter disabilityId() {
        if (disabilityId == null) {
            disabilityId = new LongFilter();
        }
        return disabilityId;
    }

    public void setDisabilityId(LongFilter disabilityId) {
        this.disabilityId = disabilityId;
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
        final PatientDisabilityCriteria that = (PatientDisabilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(disabilityId, that.disabilityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, disabilityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDisabilityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            (disabilityId != null ? "disabilityId=" + disabilityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
