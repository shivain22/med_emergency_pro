package io.aurigraph.healthcare.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PatientComorbidity.
 */
@Entity
@Table(name = "patient_comorbidity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientComorbidity implements Serializable {

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
    private Comorbidity comorbidity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PatientComorbidity id(Long id) {
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

    public PatientComorbidity patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Comorbidity getComorbidity() {
        return this.comorbidity;
    }

    public void setComorbidity(Comorbidity comorbidity) {
        this.comorbidity = comorbidity;
    }

    public PatientComorbidity comorbidity(Comorbidity comorbidity) {
        this.setComorbidity(comorbidity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientComorbidity)) {
            return false;
        }
        return id != null && id.equals(((PatientComorbidity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientComorbidity{" +
            "id=" + getId() +
            "}";
    }
}
