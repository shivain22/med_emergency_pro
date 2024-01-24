package io.aurigraph.healthcare.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PatientDisability.
 */
@Entity
@Table(name = "patient_disability")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientDisability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @ManyToOne(optional = false)
    @NotNull
    private Disability disability;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PatientDisability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientDisability patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Disability getDisability() {
        return this.disability;
    }

    public void setDisability(Disability disability) {
        this.disability = disability;
    }

    public PatientDisability disability(Disability disability) {
        this.setDisability(disability);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientDisability)) {
            return false;
        }
        return id != null && id.equals(((PatientDisability) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDisability{" +
            "id=" + getId() +
            "}";
    }
}
